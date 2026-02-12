package com.example.create_meet.data

import com.example.comon.UserDto

sealed class UsersResult {
    data class Success(val users: List<UserDto>) : UsersResult()
    data class Error(val message: String) : UsersResult()
}