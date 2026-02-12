package com.example.create_meet.domain

import com.example.comon.UserDto
import com.example.create_meet.data.CreateMeetingResult
import com.example.create_meet.data.InvitationResponse
import com.example.create_meet.data.InvitationResult
import com.example.create_meet.data.InvitationsResult
import com.example.create_meet.data.dto.MeetingResponse
import com.example.create_meet.data.MeetingsNetworkDataSource
import com.example.create_meet.data.UsersResult
import com.example.create_meet.data.dto.CreateMeetingDto
import com.example.create_meet.data.dto.MeetingsResult
import com.example.token_storage.domain.TokenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val network: MeetingsNetworkDataSource,
    private val tokenRepository: TokenRepository
) : MeetingRepository {

    private val _meetings = MutableStateFlow<List<MeetingResponse>>(emptyList())
    override val meetings: StateFlow<List<MeetingResponse>> = _meetings

    override suspend fun getUsers(): Result<List<UserDto>> {
        val token = tokenRepository.getToken() ?: return Result.failure(Exception("Token is null"))

        return when (val result = network.getUsers(token)) {

            is UsersResult.Success -> Result.success(result.users)
            is UsersResult.Error -> Result.failure(Exception(result.message))

        }
    }

    override suspend fun createMeeting(request: CreateMeetingDto): Result<MeetingResponse> {
        val token = tokenRepository.getToken() ?: return Result.failure(Exception("Token is null"))

        return when (val result = network.createMeeting(token, request)) {

            is CreateMeetingResult.Success -> {
                val updatedList = _meetings.value.toMutableList().apply { add(result.meeting) }
                _meetings.value = updatedList
                Result.success(result.meeting)
            }

            is CreateMeetingResult.Error -> Result.failure(Exception(result.message))
        }

    }

    override suspend fun getSchedule() {
        val token = tokenRepository.getToken()
        if (token != null) {
            when (val result = network.getSchedule(token)) {
                is MeetingsResult.Success -> {
                    _meetings.value = result.meetings
                }

                is MeetingsResult.Error -> {
                    throw RuntimeException(result.message)
                }
            }
        } else {
            throw RuntimeException()
        }

    }

    override suspend fun getInvitations(): Result<List<InvitationResponse>> {
        val token = tokenRepository.getToken() ?: return Result.failure(Exception("Token is null"))

        return when(val result = network.getInvitations(token)) {
            is InvitationsResult.Success -> Result.success(result.invitations)
            is InvitationsResult.Error -> Result.failure(Exception(result.message))
        }
    }

    override suspend fun acceptInvitation(invitationId: String): Result<Unit> {
        val token = tokenRepository.getToken() ?: return Result.failure(Exception("Token is null"))
        return when(val result = network.acceptInvitation(token, invitationId)) {
            InvitationResult.Success -> Result.success(Unit)
            is InvitationResult.Error -> Result.failure(Exception(result.message))
        }
    }

    override suspend fun declineInvitation(invitationId: String): Result<Unit> {
        val token = tokenRepository.getToken() ?: return Result.failure(Exception("Token is null"))
        return when(val result = network.declineInvitation(token, invitationId)) {
            InvitationResult.Success -> Result.success(Unit)
            is InvitationResult.Error -> Result.failure(Exception(result.message))
        }
    }
}


