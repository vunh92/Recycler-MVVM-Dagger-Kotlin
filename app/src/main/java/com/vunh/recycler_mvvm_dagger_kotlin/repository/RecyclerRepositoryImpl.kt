package com.vunh.recycler_mvvm_dagger_kotlin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.vunh.recycler_mvvm_dagger_kotlin.api.RecyclerService
import com.vunh.recycler_mvvm_dagger_kotlin.database.AppDatabase
import com.vunh.recycler_mvvm_dagger_kotlin.model.Movie
import com.vunh.recycler_mvvm_dagger_kotlin.model.Response
import com.vunh.recycler_mvvm_dagger_kotlin.model.Status
import com.vunh.recycler_mvvm_dagger_kotlin.usecase.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecyclerRepositoryImpl @Inject constructor(
    private val recyclerService: RecyclerService,
    private val movieDatabase: AppDatabase
    ) : RecyclerRepository {

    override suspend fun getMovieList(): UseCaseResult<List<Movie>> {
        return try {
            val movieList = withContext(Dispatchers.IO) {
                 recyclerService.callListAsync().await()
            }
            withContext(Dispatchers.IO) {
                if(movieList.isNotEmpty()) insertAll(movieList)
            }
            UseCaseResult.Success(movieList)
        } catch (ex: Throwable) {
            UseCaseResult.Error(ex.message ?: "")
        }
    }

    override suspend fun getMovie(movieId: String): UseCaseResult<Response> {
        return try {
            val result = withContext(Dispatchers.IO) {
                 recyclerService.callGetAsync(movieId = movieId).await()
            }
            UseCaseResult.Success(result)
        } catch (ex: Throwable) {
            Log.e("vunh", ex.message ?: "")
            UseCaseResult.Error(ex.message ?: "")
        }
    }

    override suspend fun insert(movie: Movie) : UseCaseResult<Status>{
        try {
            val statusResult = withContext(Dispatchers.IO) {
                recyclerService.callInsertAsync(
                    movieId = movie.movieId,
                    category = movie.category,
                    imageUrl = movie.imageUrl,
                    name = movie.name,
                    desc = movie.desc
                )
            }
            statusResult.await().let {
                if(!it.error) movieDatabase.movieDao.insert(movie)
                return UseCaseResult.Success(it)
            }
        }catch (ex: Throwable) {
            return UseCaseResult.Error(ex.message ?: "")
        }
    }

    override suspend fun update(movie: Movie) : UseCaseResult<Status>{
        try {
            val statusResult = withContext(Dispatchers.IO) {
                recyclerService.callUpdateAsync(
                    movieId = movie.movieId,
                    category = movie.category,
                    imageUrl = movie.imageUrl,
                    name = movie.name,
                    desc = movie.desc
                )
            }
            statusResult.await().let {
                if(!it.error) movieDatabase.movieDao.update(movie)
                return UseCaseResult.Success(it)
            }
        }catch (ex: Throwable) {
            return UseCaseResult.Error(ex.message ?: "")
        }
    }

    override suspend fun delete(movieId: String) : UseCaseResult<Status>{
        try {
            val statusResult = withContext(Dispatchers.IO) {
                recyclerService.callDeleteAsync(movieId = movieId)
            }
            statusResult.await().let {
                if(!it.error) movieDatabase.movieDao.delete(movieId)
                return UseCaseResult.Success(it)
            }
        }catch (ex: Throwable) {
            return UseCaseResult.Error(ex.message ?: "")
        }
    }

    override suspend fun clear() {
        movieDatabase.movieDao.clear()
    }

    override suspend fun insertAll(movies: List<Movie>) {
        movieDatabase.movieDao.insertAll(movies)
    }

    override val movies: LiveData<List<Movie>>
        get() = movieDatabase.movieDao.getAll()
}