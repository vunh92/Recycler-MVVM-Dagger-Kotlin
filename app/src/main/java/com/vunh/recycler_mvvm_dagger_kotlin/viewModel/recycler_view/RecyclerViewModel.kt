package com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.recycler_view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vunh.recycler_mvvm_dagger_kotlin.model.Movie
import com.vunh.recycler_mvvm_dagger_kotlin.repository.RecyclerRepositoryImpl
import com.vunh.recycler_mvvm_dagger_kotlin.usecase.UseCaseResult
import kotlinx.coroutines.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class RecyclerViewModel (
    private val recyclerRepositoryImpl: RecyclerRepositoryImpl
    ) : ViewModel() , CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val showLoading = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String>()
    val showResult = MutableLiveData<Boolean>()

    val movieList = recyclerRepositoryImpl.movies

    init {
        getMovieList()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getMovieList(){
        showLoading.value = true
        viewModelScope.launch {
            try {
                var result = withContext(Dispatchers.IO){
                    recyclerRepositoryImpl.getMovieList()
                }
                showLoading.value= false
                when (result) {
                    is UseCaseResult.Success -> {
                        showResult.value = true
                        if(result.data.isNotEmpty()){
                            withContext(Dispatchers.IO){
                                recyclerRepositoryImpl.insertAll(result.data)
                            }
                        }
                    }
                    is UseCaseResult.Error -> {
                        showResult.value = false
                        showError.value = result.errorMessage
                    }
                }

            } catch (networkError: IOException) {
                showLoading.value= false
                showResult.value = false
//                if(movieList.value.isNullOrEmpty())
//                    _eventNetworkError.value = true
            }

        }
    }

    fun deleteMovie(movieId: String) {
        showLoading.value = true
        viewModelScope.launch {
            try {
                var result = withContext(Dispatchers.IO){
                    recyclerRepositoryImpl.delete(movieId)
                }
                showLoading.value= false
                when (result) {
                    is UseCaseResult.Success -> {
                        showResult.value = !result.data.error
                    }
                    is UseCaseResult.Error -> {
                        showResult.value = false
                        showError.value = result.errorMessage
                    }
                }
            } catch (networkError: IOException) {
                showLoading.value= false
                showResult.value = false
//                if(movieList.value.isNullOrEmpty())
//                    _eventNetworkError.value = true
            }
        }
    }

    fun clearMovie() {
        viewModelScope.launch {
            recyclerRepositoryImpl.clear()
        }
    }
}