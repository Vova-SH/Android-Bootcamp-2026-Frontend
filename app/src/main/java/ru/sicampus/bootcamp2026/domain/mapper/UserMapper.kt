package ru.sicampus.bootcamp2026.domain.mapper


import ru.sicampus.bootcamp2026.data.dto.user.UserDto
import ru.sicampus.bootcamp2026.data.dto.user.UserMiniDto
import ru.sicampus.bootcamp2026.data.dto.user.UserUpdateDto
import ru.sicampus.bootcamp2026.domain.entities.User
import ru.sicampus.bootcamp2026.domain.entities.UserMini
import ru.sicampus.bootcamp2026.domain.entities.UserMiniInvitation
import ru.sicampus.bootcamp2026.domain.entities.UserUpdate
import java.time.LocalDateTime

object UserMapper {

    fun toEntity(userDto: UserDto): User {
        return User(
            id = userDto.id,
            firstName = userDto.firstName,
            secondName = userDto.secondName,
            description = userDto.description,
            position = userDto.position,
            department = userDto.department,
            photoUrl = userDto.photoUrl,
            role = userDto.role,
            createdAt = LocalDateTime.parse(userDto.createdAt),
            updatedAt = LocalDateTime.parse(userDto.updatedAt),
            email = userDto.email
        )
    }

    fun toDomain(dto: UserMiniDto): UserMini {
        return UserMini(
            id = dto.id,
            firstName = dto.firstName,
            secondName = dto.secondName,
            photoUrl = dto.photoUrl
        )
    }

    fun toDomain(dto: UserUpdateDto): UserUpdate {
        return UserUpdate(
            id = dto.id,
            firstName = dto.firstName,
            secondName = dto.secondName,
            description = dto.description,
            position = dto.position,
            department = dto.department
        )
    }

    fun toDto(domain: UserUpdate): UserUpdateDto {
        return UserUpdateDto(
            id = domain.id,
            firstName = domain.firstName,
            secondName = domain.secondName,
            description = domain.description,
            position = domain.position,
            department = domain.department
        )
    }

    fun toDto(domain: User): UserDto {
        return UserDto(
            id = domain.id,
            firstName = domain.firstName,
            secondName = domain.secondName,
            email = domain.email,
            description = domain.description,
            position = domain.position,
            department = domain.department,
            photoUrl = domain.photoUrl,
            role = domain.role,
            createdAt = domain.createdAt.toString(),
            updatedAt = domain.updatedAt.toString()
        )
    }
}