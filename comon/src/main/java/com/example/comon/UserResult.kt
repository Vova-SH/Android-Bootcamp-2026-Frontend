package com.example.comon

sealed class UserResult {
    data class Success(val user: User) : UserResult()
    data class Error(val message: String) : UserResult()

    object NotLoaded : UserResult()
}