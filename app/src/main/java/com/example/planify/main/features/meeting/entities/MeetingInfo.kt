package com.example.planify.main.features.meeting.entities

import com.example.planify.main.features.profile.entities.Profile

data class MeetingInfo(
    val meeting: Meeting,
    val invites: List<Invite>,
    val participants: List<Profile>
)