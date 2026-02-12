package ru.sicampus.bootcamp2026.domain.users.entities

data class PagingUserListEntity (
    val isLast: Boolean,
    val users: List<UserEntity>
)