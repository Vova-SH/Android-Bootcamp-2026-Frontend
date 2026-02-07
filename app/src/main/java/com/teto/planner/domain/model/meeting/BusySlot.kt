package com.teto.planner.domain.model.meeting

data class BusySlot(
    val hour: Int,
    val meetingId: String?
)