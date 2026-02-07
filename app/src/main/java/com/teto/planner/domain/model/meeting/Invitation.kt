package com.teto.planner.domain.model.meeting

import com.teto.planner.domain.model.user.ParticipantRole
import com.teto.planner.domain.model.user.ParticipantStatus

data class Invitation(
    val meeting: Meeting,
    val myRole: ParticipantRole,
    val myStatus: ParticipantStatus
)