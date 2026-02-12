package com.example.planify.main.features.auth.domain.entities

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val context: AuthContext, val tokenPair: AuthTokenPair) : AuthState()
}
