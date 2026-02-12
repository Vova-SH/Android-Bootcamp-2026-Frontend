package com.example.comon

sealed class GetUserResult {
    data class Success(val user: UserDto) : GetUserResult()
    data class Error(val message: String) : GetUserResult()
}