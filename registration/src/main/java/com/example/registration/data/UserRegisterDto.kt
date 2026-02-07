package com.example.registration.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterDto(
    @SerialName("fullName")
    val fullName: String,

    @SerialName("phoneNumber")
    val phoneNumber: String,

    @SerialName("department")
    val department: String,

    @SerialName("password")
    val password: String
)