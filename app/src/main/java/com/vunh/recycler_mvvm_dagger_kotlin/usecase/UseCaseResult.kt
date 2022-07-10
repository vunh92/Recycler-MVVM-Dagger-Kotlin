package com.vunh.recycler_mvvm_dagger_kotlin.usecase

import com.google.gson.annotations.SerializedName

sealed class UseCaseResult<out T : Any> {
    class Success<out T : Any>(val data: T) : UseCaseResult<T>()
    class Error(val errorMessage: String = "",val errorMessageKor : String?="") : UseCaseResult<Nothing>()
}