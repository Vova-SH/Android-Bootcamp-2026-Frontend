package com.example.planify.main.features.auth.domain.schemas

data class RegisterUserSchema (
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
)