package com.example.token_storage.domain.use_cases

import com.example.token_storage.domain.TokenRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke() = tokenRepository.getToken()
}