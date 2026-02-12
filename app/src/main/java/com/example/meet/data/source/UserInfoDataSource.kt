package com.example.meet.data.source

import com.example.meet.data.dto.CreateMeetingDto
import com.example.meet.data.dto.MeetingDto
import com.example.meet.data.dto.NotificationDto
import com.example.meet.data.dto.UserDto
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class UserInfoDataSource {

    private val currentUserId: Long
        get() = Network.currentUserId ?: error("Пользователь не авторизован")

    suspend fun loadCurrentUser(): UserDto =
        Network.getUserById(currentUserId)

    suspend fun markNotificationAsRead(notificationId: Long) {
        Network.markNotificationAsRead(notificationId)
    }
    suspend fun loadAllUsers(): List<UserDto> =
        Network.getUsers()

    suspend fun getUserById(userId: Long): UserDto =
        Network.getUserById(userId)

    suspend fun loadAllMeetings(): List<MeetingDto> =
        Network.getMeetings()

    suspend fun loadMeetingsForCurrentUser(): List<MeetingDto> {
        val userId = currentUserId
        val allMeetings = Network.getMeetings()
        val invitations = Network.getInvitations()
        val invitedMeetingIds = invitations
            .filter { it.userId == userId }
            .map { it.meetingId }
            .toSet()
        return allMeetings.filter { it.organizerId == userId || invitedMeetingIds.contains(it.id) }
    }

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
        return meetingDataSource.createMeeting(dto)
    }

    val userInfoDataSource: UserInfoDataSource by lazy {
        UserInfoDataSource()
    }
    val invitationDataSource: InvitationDataSource by lazy {
        InvitationDataSource()
    }
    val meetingDataSource: MeetingDataSource by lazy {
        MeetingDataSource()
    }
}
