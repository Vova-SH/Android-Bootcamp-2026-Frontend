package ru.sicampus.bootcamp2026.domain.entities

data class PagingUserListEntity(
    val isLast: Boolean,
    val users: List<UserEntity>
)