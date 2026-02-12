package com.example.planify.main.features.profiles.domain.entities

data class Profile(
    val userId: Long = 0L,
    val firstName: String,
    val lastName: String,
    val position: String,
    val department: String,
    val profileImageUrl: String
)