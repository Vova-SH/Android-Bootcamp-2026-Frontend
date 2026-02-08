package com.example.planify.main.features.profiles.domain.schemas

data class PutMyProfileSchema (
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val position: String,
    val department: String,
    val profileImageUrl: String
)