package ru.sicampus.bootcamp2026.data.model

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("name") val name: String,
    @SerializedName("position") val position: String,
    @SerializedName("login") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?
)