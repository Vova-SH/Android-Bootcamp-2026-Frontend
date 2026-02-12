package com.example.planify.main.features.meetings.domain.entities

data class MeetingWithParticipantIds(
    val meeting: Meeting,
    val participantIds: List<Long>
)
