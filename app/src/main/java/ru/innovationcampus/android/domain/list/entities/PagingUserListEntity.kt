package ru.innovationcampus.android.domain.list.entities

data class PagingUserListEntity(
    val isLast: Boolean,
    val users: List<UserEntity>
)