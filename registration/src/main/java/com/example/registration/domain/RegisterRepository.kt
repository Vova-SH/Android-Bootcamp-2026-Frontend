package com.example.registration.domain

import com.example.comon.RegisterResult

interface RegisterRepository {
    suspend fun register(user: UserRegisterEntity): RegisterResult
}