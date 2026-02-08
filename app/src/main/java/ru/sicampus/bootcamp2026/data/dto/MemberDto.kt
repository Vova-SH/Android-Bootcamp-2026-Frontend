package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MemberDto(
    val member: UserDto,
    val status: String
)