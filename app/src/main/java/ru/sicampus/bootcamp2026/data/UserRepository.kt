package ru.sicampus.bootcamp2026.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.data.dto.InvitationDto
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.data.source.MeetingDto
import ru.sicampus.bootcamp2026.data.source.Network
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.domain.entities.toEntity
import ru.sicampus.bootcamp2026.domain.entities.toDto
import java.net.HttpURLConnection
import java.net.URL

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

    suspend fun getCurrentUser(username: String): Result<UserEntity> {
        return userInfoDataSource.getUserByUsername(username).map { it.toEntity() }
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

    suspend fun login(username: String, password: String): Result<UserEntity> {
        return userInfoDataSource.login(username, password)
            .map { userDto ->
                CredentialsHolder.username = username
                CredentialsHolder.password = password
                userDto.toEntity()
            }
    }

    private fun saveCredentials(username: String, password: String) {
        // Простейший вариант — в памяти
        CredentialsHolder.username = username
        CredentialsHolder.password = password
    }

    suspend fun getInvitationsByPersonId(id: Long): Result<List<InvitationDto>> {
        return userInfoDataSource.getInvitationsByPersonId(id)
    }
}

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserDto
)

object CredentialsHolder {
    var username: String = ""
    var password: String = ""
}