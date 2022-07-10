package com.vunh.recycler_mvvm_dagger_kotlin.viewmodel.detail_movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vunh.recycler_mvvm_dagger_kotlin.repository.RecyclerRepositoryImpl
import javax.inject.Inject

class DetailMovieViewModelFactory @Inject constructor(
    private val recyclerRepositoryImpl: RecyclerRepositoryImpl
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DetailMovieViewModel::class.java)) {
            DetailMovieViewModel(recyclerRepositoryImpl) as T
        }else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}