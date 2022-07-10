package com.vunh.recycler_mvvm_dagger_kotlin.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Response(
    @SerializedName("status")
    var status: Status,
    @SerializedName("data")
    var data: Any,
) : Serializable
