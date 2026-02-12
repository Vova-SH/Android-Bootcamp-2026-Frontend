package com.example.planify.main.common.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    @SerialName("ok")
    val ok: Boolean,

    @SerialName("appCode")
    val appCode: Int,

    @SerialName("message")
    val message: String,

    @SerialName("data")
    val data: T? = null,
)
