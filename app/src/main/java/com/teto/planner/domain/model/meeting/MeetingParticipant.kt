package com.teto.planner.domain.model.meeting

import com.teto.planner.domain.model.user.ParticipantRole
import com.teto.planner.domain.model.user.ParticipantStatus
import com.teto.planner.domain.model.user.UserSummary
import java.time.LocalDateTime

data class MeetingParticipant(
    val user: UserSummary,
    val role: ParticipantRole,
    val status: ParticipantStatus,
    val joinedAt: LocalDateTime? = null,
    val respondedAt: LocalDateTime? = null
)