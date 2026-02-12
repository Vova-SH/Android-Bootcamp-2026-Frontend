package com.example.planify.main.features.auth.domain.services_impl

import com.example.planify.core.SingletonHolder
import com.example.planify.main.features.auth.domain.repositories.AuthRepository
import com.example.planify.main.features.auth.domain.services.AuthService

class AuthServiceImplST private constructor (
    val authRepository: AuthRepository
) : AuthService {
    override fun isAuthorized(): Boolean {
        return authRepository.hasTokens()
    }

    companion object : SingletonHolder<AuthServiceImplST, AuthRepository>(::AuthServiceImplST)
}