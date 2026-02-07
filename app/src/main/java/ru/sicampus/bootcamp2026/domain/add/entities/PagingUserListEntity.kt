package ru.sicampus.bootcamp2026.domain.add.entities

import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity


data class PagingUserListEntity(
    val isLast: Boolean,
    val users: List<UserEntity>
)