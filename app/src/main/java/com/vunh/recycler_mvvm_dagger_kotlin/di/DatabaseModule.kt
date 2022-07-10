package com.vunh.recycler_mvvm_dagger_kotlin.di

import android.content.Context
import androidx.room.Room
import com.vunh.recycler_mvvm_dagger_kotlin.database.AppDatabase
import com.vunh.recycler_mvvm_dagger_kotlin.database.MovieDao
import com.vunh.recycler_mvvm_dagger_kotlin.utils.AppUtils
import com.vunh.recycler_mvvm_dagger_kotlin.utils.Constant
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(appContext: Context) : AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            Constant.DATABASENAME,
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase) : MovieDao {
        return appDatabase.movieDao
    }
}