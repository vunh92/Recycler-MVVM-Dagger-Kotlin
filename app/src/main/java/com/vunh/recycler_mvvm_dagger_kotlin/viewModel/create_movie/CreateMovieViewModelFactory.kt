package com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.create_movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vunh.recycler_mvvm_dagger_kotlin.repository.RecyclerRepositoryImpl
import javax.inject.Inject

class CreateMovieViewModelFactory @Inject constructor(
    private val recyclerRepositoryImpl: RecyclerRepositoryImpl
    ) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CreateMovieViewModel::class.java)) {
            CreateMovieViewModel(recyclerRepositoryImpl) as T
        }else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}