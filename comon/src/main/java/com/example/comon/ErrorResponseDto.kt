package com.example.comon

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(
    val message: String
)