package com.teto.planner.domain.model.meeting

import com.teto.planner.domain.model.user.UserSummary
import java.time.LocalDate

data class IntersectionSlot(
    val hour: Int,
    val status: IntersectionSlotStatus,
    val conflictedUsers: List<UserSummary> = emptyList(),
    val label: String?
)

enum class IntersectionSlotStatus {
    GREEN,
    YELLOW,
    DISABLED
}

data class IntersectionResponse(
    val date: LocalDate,
    val organizer: UserSummary?,
    val users: List<UserSummary>,
    val slots: List<IntersectionSlot>
)