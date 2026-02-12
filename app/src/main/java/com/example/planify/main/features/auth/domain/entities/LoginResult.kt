package com.example.planify.main.features.auth.domain.entities

class LoginResult(
    val authContext: AuthContext,
    val tokens: AuthTokenPair
)
