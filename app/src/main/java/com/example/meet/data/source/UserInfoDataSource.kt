package com.example.meet.data.source

import com.example.meet.data.dto.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class UserInfoDataSource {

    private val currentUserId: Long
        get() = Network.currentUserId ?: error("Пользователь не авторизован")

    suspend fun loadCurrentUser(): UserDto =
        Network.getUserById(currentUserId)

    suspend fun loadAllUsers(): List<UserDto> =
        Network.getUsers()

    suspend fun searchUsers(query: String): List<UserDto> {
        val allUsers = loadAllUsers()
        return if (query.isBlank()) {
            allUsers
        } else {
            allUsers.filter { user ->
                user.fullName.contains(query, ignoreCase = true) ||
                        user.email.contains(query, ignoreCase = true) ||
                        user.position?.contains(query, ignoreCase = true) == true ||
                        user.department?.contains(query, ignoreCase = true) == true
            }
        }
    }

    suspend fun loadAllMeetings(): List<MeetingDto> =
        Network.getMeetings()

    suspend fun loadMeetingsForCurrentUser(): List<MeetingDto> =
        Network.getMeetingsForUser(currentUserId)

    suspend fun loadActiveInvitations(): List<InvitationDto> =
        Network.getActiveInvitations(currentUserId)

    suspend fun loadNotifications(): List<NotificationDto> =
        Network.getNotifications(currentUserId)

    suspend fun registerUser(
        email: String,
        password: String,
        fullName: String,
        position: String? = null,
        department: String? = null
    ): UserDto =
        Network.register(email, password, fullName, position, department)
}

@ExperimentalSerializationApi
object DataLocator {
    suspend fun createMeeting(dto: CreateMeetingDto): Result<MeetingDto> {
        return try {
            val meeting = Network.createMeeting(dto)
            Result.success(meeting)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    val userInfoDataSource: UserInfoDataSource by lazy {
        UserInfoDataSource()
    }
    val invitationDataSource: InvitationDataSource by lazy {
        InvitationDataSource()
    }
}