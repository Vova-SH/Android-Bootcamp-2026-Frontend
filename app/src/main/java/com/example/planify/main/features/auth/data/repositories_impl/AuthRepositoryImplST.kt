package com.example.planify.main.features.auth.data.repositories_impl

import com.example.planify.core.SingletonHolder
import com.example.planify.main.features.auth.domain.repositories.AuthRepository

object AuthRepositoryImplST : AuthRepository {
    override fun hasTokens(): Boolean {
        return true
    }
}