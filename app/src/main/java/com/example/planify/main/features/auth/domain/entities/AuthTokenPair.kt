package com.example.planify.main.features.auth.domain.entities

data class AuthTokenPair(
    val accessToken: String,
    val refreshToken: String
)
