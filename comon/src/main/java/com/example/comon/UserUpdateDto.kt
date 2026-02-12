package com.example.comon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateDto(
    @SerialName("fullName")
    val fullName: String,

    @SerialName("department")
    val department: String,
)