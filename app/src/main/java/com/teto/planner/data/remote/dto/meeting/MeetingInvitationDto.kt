package com.teto.planner.data.remote.dto.meeting

import com.teto.planner.domain.model.meeting.Invitation
import com.teto.planner.domain.model.user.ParticipantRole
import com.teto.planner.domain.model.user.ParticipantStatus
import kotlinx.serialization.Serializable

@Serializable
data class InvitationDto(
    val meeting: MeetingDto,
    val myRole: String,
    val myStatus: String
)

@Serializable
data class InvitationResponseRequest(
    val status: String
)


fun InvitationDto.toDomain() = Invitation(
    meeting = meeting.toDomain(),
    myRole = ParticipantRole.valueOf(myRole),
    myStatus = ParticipantStatus.valueOf(myStatus)
)