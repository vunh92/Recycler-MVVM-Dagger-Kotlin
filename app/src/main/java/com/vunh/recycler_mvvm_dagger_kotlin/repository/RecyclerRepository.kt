package com.vunh.recycler_mvvm_dagger_kotlin.repository

import androidx.lifecycle.LiveData
import com.vunh.recycler_mvvm_dagger_kotlin.model.Movie
import com.vunh.recycler_mvvm_dagger_kotlin.model.Response
import com.vunh.recycler_mvvm_dagger_kotlin.model.Status
import com.vunh.recycler_mvvm_dagger_kotlin.usecase.UseCaseResult

interface RecyclerRepository {
    suspend fun getMovieList(): UseCaseResult<List<Movie>>
    suspend fun getMovie(movieId: String): UseCaseResult<Response>
    suspend fun insert(movie: Movie) : UseCaseResult<Status>
    suspend fun update(movie: Movie) : UseCaseResult<Status>
    suspend fun delete(movieId: String) : UseCaseResult<Status>
    suspend fun clear()
    suspend fun insertAll(movies: List<Movie>)
    val movies: LiveData<List<Movie>>
}