package ru.sicampus.bootcamp2026.domain.entities

import ru.sicampus.bootcamp2026.data.dto.UserDto

data class UserEntity(
    val id: Long? = null,
    val name: String,
    val email: String,
    val photoUrl: String? = null,
    val departmentName: String? = null,
    val createdAt: String? = null
)

fun UserDto.toEntity(): UserEntity = UserEntity(
    id = this.id,
    name = this.name,
    email = this.email,
    photoUrl = this.photoUrl,
    departmentName = this.departmentName,
    createdAt = this.createdAt
)

fun UserEntity.toDto(): UserDto = UserDto(
    id = this.id,
    name = this.name,
    email = this.email,
    photoUrl = this.photoUrl,
    departmentName = this.departmentName,
    createdAt = this.createdAt
)