package com.example.registration.domain

import com.example.comon.RegisterResult
import com.example.comon.UserEntity
import com.example.registration.data.RegisterNetworkDataSource
import com.example.token_storage.domain.TokenRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val registerNetworkDataSource: RegisterNetworkDataSource,
    private val tokenRepository: TokenRepository
) : RegisterRepository {
    override suspend fun register(user: UserEntity): RegisterResult {
        return when (val result = registerNetworkDataSource.registerUser(user)) {
            is RegisterResult.Success -> {
                tokenRepository.saveAccessToken(result.data.accessToken)
                RegisterResult.Success(result.data)
            }
            is RegisterResult.Error -> {
                result
            }
        }
    }
}