package com.example.meet.data.mapper

import com.example.meet.data.dto.UserDto
import com.example.meet.domain.entity.User
import java.time.format.DateTimeFormatter

object UserMapper {
    fun toDomain(dto: UserDto): User {
        return User(
            id = dto.id.toInt(),
            email = dto.email,
            fullName = dto.fullName,
            position = dto.position,
            department = dto.department,
            avatarUrl = dto.avatarUrl,
            role = dto.role,
            isActive = dto.isActive,

            workHoursStart = dto.workHoursStart,
            workHoursEnd = dto.workHoursEnd
        )
    }

    fun toDto(domain: User): UserDto {
        return UserDto(
            id = domain.id.toLong(),
            email = domain.email,
            fullName = domain.fullName,
            position = domain.position,
            department = domain.department,
            avatarUrl = domain.avatarUrl,
            role = domain.role,
            isActive = domain.isActive,

            workHoursStart = domain.workHoursStart?.format(DateTimeFormatter.ISO_LOCAL_TIME),
            workHoursEnd = domain.workHoursEnd?.format(DateTimeFormatter.ISO_LOCAL_TIME)
        )
    }

    fun toDomainList(dtoList: List<UserDto>): List<User> {
        return dtoList.map { toDomain(it) }
    }
}