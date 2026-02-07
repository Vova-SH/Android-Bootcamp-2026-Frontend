package com.example.comon

sealed class RegisterResult {
    data class Success(val data: LoginResponseDto) : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}