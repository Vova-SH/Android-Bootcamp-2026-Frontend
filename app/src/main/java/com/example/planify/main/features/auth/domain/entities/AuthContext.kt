package com.example.planify.main.features.auth.domain.entities

data class AuthContext(
    val session: AuthSession,
    val user: UserPrivate,
    val accessInfo: AccessInfo
)
