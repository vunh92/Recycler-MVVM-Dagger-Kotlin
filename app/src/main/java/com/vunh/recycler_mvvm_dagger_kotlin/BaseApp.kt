package com.vunh.recycler_mvvm_dagger_kotlin

import android.app.Application
import com.vunh.recycler_mvvm_dagger_kotlin.api.RecyclerService
import com.vunh.recycler_mvvm_dagger_kotlin.database.AppDatabase
import com.vunh.recycler_mvvm_dagger_kotlin.di.AppComponent
import com.vunh.recycler_mvvm_dagger_kotlin.di.DaggerAppComponent
import com.vunh.recycler_mvvm_dagger_kotlin.repository.RecyclerRepositoryImpl

class BaseApp : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
//    companion object {
//        var baseApp: BaseApp? = null
//    }
//
//    lateinit var recyclerService: RecyclerService
//    lateinit var movieDatabase: AppDatabase
//    lateinit var recyclerRepositoryImpl: RecyclerRepositoryImpl
//
//    override fun onCreate() {
//        super.onCreate()
//        baseApp = this
//        recyclerService = RecyclerService.getInstance()
//        movieDatabase = AppDatabase.getInstance(this)
//        recyclerRepositoryImpl = RecyclerRepositoryImpl(
//            recyclerService,
//            movieDatabase
//        )
//    }
}