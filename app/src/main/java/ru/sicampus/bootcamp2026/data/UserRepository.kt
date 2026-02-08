package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.data.source.MeetingDto
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.domain.entities.toEntity
import ru.sicampus.bootcamp2026.domain.entities.toDto

open class UserRepository(
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun getUsers(): Result<List<UserEntity>> {
        return userInfoDataSource.getAllUsers().map { listDto ->
            listDto.map { it.toEntity() }
        }
    }

    suspend fun getUserById(id: Long): Result<UserEntity> {
        return userInfoDataSource.getUserById(id).map { it.toEntity() }
    }

    suspend fun registerUser(user: UserEntity): Result<UserEntity> {
        return userInfoDataSource.registerUser(user.toDto()).map { it.toEntity() }
    }

    suspend fun updateUser(id: Long, user: UserEntity): Result<UserEntity> {
        return userInfoDataSource.updateUser(id, user.toDto()).map { it.toEntity() }
    }

    suspend fun deleteUser(id: Long): Result<Unit> {
        return userInfoDataSource.deleteUser(id)
    }

    suspend fun getAllMeetings(): Result<List<MeetingDto>> {
        return userInfoDataSource.getAllMeetings()
    }

    suspend fun getMeetingsByDate(date: String): Result<List<MeetingDto>> {
        return userInfoDataSource.getMeetingsByDate(date)
    }
}