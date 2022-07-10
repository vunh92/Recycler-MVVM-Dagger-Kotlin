package com.vunh.recycler_mvvm_dagger_kotlin.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Status (
    @SerializedName("type")
    var type: String,
    @SerializedName("message")
    var message: String,
    @SerializedName("code")
    var code: Int,
    @SerializedName("error")
    var error: Boolean,
) : Serializable