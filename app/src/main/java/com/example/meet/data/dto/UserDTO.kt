package com.example.meet.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Long = 0L,

    @SerialName("email")
    val email: String = "",

    @SerialName("passwordHash")
    val passwordHash: String? = null,

    @SerialName("fullName")
    val fullName: String = "",

    @SerialName("position")
    val position: String? = null,

    @SerialName("department")
    val department: String? = null,

    @SerialName("avatarUrl")
    val avatarUrl: String? = null,

    @SerialName("role")
    val role: String = "USER",

    @SerialName("notificationSettings")
    val notificationSettings: String? = null,

    @SerialName("workHoursStart")
    val workHoursStart: String? = null,

    @SerialName("workHoursEnd")
    val workHoursEnd: String? = null,

    @SerialName("createdAt")
    val createdAt: String? = null,

    @SerialName("updatedAt")
    val updatedAt: String? = null,


    @SerialName("active")
    val isActive: Boolean = true
)