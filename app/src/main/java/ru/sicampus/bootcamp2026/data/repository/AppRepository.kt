package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.auth.TokenStorage
import ru.sicampus.bootcamp2026.data.model.CreateMeetingRequest
import ru.sicampus.bootcamp2026.data.model.InvitationDecisionRequest
import ru.sicampus.bootcamp2026.data.model.MeetingDto
import ru.sicampus.bootcamp2026.data.model.MeetingParticipantDto
import ru.sicampus.bootcamp2026.data.model.PageResponse
import ru.sicampus.bootcamp2026.data.model.UpdateUserRequest
import ru.sicampus.bootcamp2026.data.model.UserDto
import ru.sicampus.bootcamp2026.data.network.AppApi

class AppRepository(
    private val api: AppApi,
    private val tokenStorage: TokenStorage
) {
    private fun getAuthHeader(): String {
        val token = tokenStorage.getToken() ?: ""
        return "Basic $token"
    }

    suspend fun getUsers(page: Int, size: Int): Result<PageResponse<UserDto>> = runCatching {
        api.getUsers(getAuthHeader(), page, size)
    }

    suspend fun createMeeting(
        title: String,
        start: String,
        end: String,
        description: String?,
        ids: List<Long>
    ): Result<MeetingDto> = runCatching {
        val req = CreateMeetingRequest(title, start, end, description, ids)
        api.createMeeting(getAuthHeader(), req)
    }

    suspend fun getMeetings(): Result<List<MeetingDto>> = runCatching {
        api.getMeetings(getAuthHeader())
    }

    suspend fun getMeetingParticipants(meetingId: Long): Result<List<MeetingParticipantDto>> = runCatching {
        api.getMeetingParticipants(getAuthHeader(), meetingId)
    }

    suspend fun getCurrentUser(): Result<UserDto> = runCatching {
        api.getMe(getAuthHeader())
    }

    suspend fun updateUser(
        id: Long,
        name: String,
        position: String,
        email: String,
        phone: String?,
        birthDate: String?,
        avatarUrl: String?
    ): Result<UserDto> = runCatching {
        val req = UpdateUserRequest(name, position, email, phone, birthDate, avatarUrl)
        api.updateUser(getAuthHeader(), id, req)
    }

    suspend fun answerInvite(userId: Long, meetingId: Long, isAccepted: Boolean) = runCatching {
        val status = if (isAccepted) "ACCEPTED" else "REJECTED"
        api.decideInvitation(getAuthHeader(), userId, meetingId, InvitationDecisionRequest(status))
    }

    suspend fun getUsers(page: Int, size: Int, search: String?): Result<PageResponse<UserDto>> = runCatching {
        api.getUsers(getAuthHeader(), page, size, search)
    }

    suspend fun getInvites(userId: Long, page: Int) = runCatching {
        api.getInvites(getAuthHeader(), userId, "PENDING", page, 10)
    }
}