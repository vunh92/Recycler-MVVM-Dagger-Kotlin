package com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.detail_movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vunh.recycler_mvvm_dagger_kotlin.utils.AppUtils
import com.vunh.recycler_mvvm_dagger_kotlin.model.Movie
import com.vunh.recycler_mvvm_dagger_kotlin.repository.RecyclerRepositoryImpl
import com.vunh.recycler_mvvm_dagger_kotlin.usecase.UseCaseResult
import kotlinx.coroutines.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class DetailMovieViewModel (
    private val recyclerRepositoryImpl: RecyclerRepositoryImpl
) : ViewModel() , CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    val showLoading = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String>()
    val showResult= MutableLiveData<Movie>()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getMovie(movieId: String){
        showLoading.value = true
        viewModelScope.launch {
            try {
                var result = withContext(Dispatchers.IO){
                    recyclerRepositoryImpl.getMovie(movieId)
                }
                showLoading.value= false
                when (result) {
                    is UseCaseResult.Success -> {
                        var movieMap: Map<String, Any> = result.data.data as Map<String, Any>
                        showResult.value = AppUtils.mapToMovie(movieMap)
                        //Todo: chỉnh sửa datetime sau này

//                            val stringDate = "2021-12-16T16:42:00.000Z"
//                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//                            var consultationDate = sdf.parse(stringDate)
//                            Log.d("vunh", "consultationDate : " + consultationDate)
//                            val format = SimpleDateFormat("dd/MM/yyy")
//                            var date = format.format(consultationDate)
//                            Log.d("vunh", "date : " + date)
                    }
                    is UseCaseResult.Error -> {
                        showError.value = result.errorMessage
                    }
                }
            } catch (networkError: IOException) {
                showLoading.value= false
            }

        }
    }
}