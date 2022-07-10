package com.vunh.recycler_mvvm_dagger_kotlin.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vunh.recycler_mvvm_dagger_kotlin.api.RecyclerService
import com.vunh.recycler_mvvm_dagger_kotlin.utils.Constant
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ServiceModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideRecyclerService(retrofit: Retrofit): RecyclerService {
        return retrofit.create(RecyclerService::class.java)
    }
}