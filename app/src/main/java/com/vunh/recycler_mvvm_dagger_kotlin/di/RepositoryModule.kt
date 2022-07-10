package com.vunh.recycler_mvvm_dagger_kotlin.di

import com.vunh.recycler_mvvm_dagger_kotlin.repository.RecyclerRepository
import com.vunh.recycler_mvvm_dagger_kotlin.repository.RecyclerRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun provideRecyclerRepository(recyclerRepositoryImpl: RecyclerRepositoryImpl): RecyclerRepository
}