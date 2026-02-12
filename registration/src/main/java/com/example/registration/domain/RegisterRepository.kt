package com.example.registration.domain

import com.example.comon.RegisterResult
import com.example.comon.UserEntity

interface RegisterRepository {
    suspend fun register(user: UserEntity): RegisterResult
}