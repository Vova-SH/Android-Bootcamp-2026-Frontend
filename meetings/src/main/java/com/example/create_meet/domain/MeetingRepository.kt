package com.example.create_meet.domain

import com.example.comon.UserDto
import com.example.create_meet.data.InvitationResponse
import com.example.create_meet.data.dto.CreateMeetingDto
import com.example.create_meet.data.dto.MeetingResponse
import kotlinx.coroutines.flow.StateFlow


interface MeetingRepository {
    suspend fun getSchedule()

    val meetings: StateFlow<List<MeetingResponse>>

    suspend fun getUsers(): Result<List<UserDto>>

    suspend fun createMeeting(request: CreateMeetingDto): Result<MeetingResponse>
    suspend fun acceptInvitation(invitationId: String): Result<Unit>
    suspend fun declineInvitation(invitationId: String): Result<Unit>
    suspend fun getInvitations(): Result<List<InvitationResponse>>

}

