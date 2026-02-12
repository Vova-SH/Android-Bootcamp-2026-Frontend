package com.example.planify.main.features.auth.domain.repositories

import com.example.planify.main.features.auth.domain.entities.UserPrivate

interface UsersRepository {
    suspend fun fetchMe(): Result<UserPrivate>
}