package com.example.authorization.domain

import com.example.authorization.data.dto.AuthNetworkDataSource
import com.example.authorization.domain.entites.UserLoginEntity
import com.example.comon.RegisterResult
import com.example.token_storage.domain.TokenRepository
import javax.inject.Inject
import kotlin.onSuccess

class AuthRepositoryImpl @Inject constructor(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val tokenRepository: TokenRepository
) : AuthRepository {

    override suspend fun authorize(user: UserLoginEntity): RegisterResult {
        return when (val result = authNetworkDataSource.checkAuth(user)) {
            is RegisterResult.Success -> {
                tokenRepository.saveAccessToken(result.data.accessToken)
                result
            }
            is RegisterResult.Error -> result
        }
    }
}