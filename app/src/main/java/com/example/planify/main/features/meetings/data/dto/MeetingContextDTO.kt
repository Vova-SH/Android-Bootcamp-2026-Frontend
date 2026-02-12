package com.example.planify.main.features.meetings.data.dto

import com.example.planify.main.common.dto.ProfileDTO
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingContextDTO(
    @SerialName("invitedUserProfiles")
    val invitedUserProfiles: List<ProfileDTO>,

    @SerialName("participantProfiles")
    val participantProfiles: List<ProfileDTO>,

    @SerialName("invites")
    val invites: List<MeetingInviteDTO>,

    @SerialName("meeting")
    val meeting: MeetingDTO
) {
    fun toEntity(): MeetingContext = MeetingContext(
        invitedUserProfiles = invitedUserProfiles.map { it.toEntity() },
        participantProfiles = participantProfiles.map { it.toEntity() },
        invites = invites.map { it.toEntity() },
        meeting = meeting.toEntity(),
    )
}
