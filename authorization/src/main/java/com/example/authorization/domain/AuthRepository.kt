package com.example.authorization.domain

import com.example.authorization.domain.entites.UserLoginEntity
import com.example.comon.RegisterResult

interface AuthRepository {
    suspend fun authorize(user: UserLoginEntity): RegisterResult
}