package com.example.meet.domain.entity

data class User(
    val id: Int,
    val email: String,
    val fullName: String,
    val position: String? = null,
    val department: String? = null,
    val avatarUrl: String? = null,
    val role: String = "USER",
    val workHoursStart: String? = null,
    val workHoursEnd: String? = null,
    val isActive: Boolean = true,
)