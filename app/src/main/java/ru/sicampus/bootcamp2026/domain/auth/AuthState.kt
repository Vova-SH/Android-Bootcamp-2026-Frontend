package ru.sicampus.bootcamp2026.domain.auth

sealed interface AuthState {
    data object Loading : AuthState
    data object Authorized : AuthState
    data object Unauthorized : AuthState
}