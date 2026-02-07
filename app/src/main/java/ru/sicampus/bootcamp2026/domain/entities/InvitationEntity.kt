package ru.sicampus.bootcamp2026.domain.entities

data class InvitationEntity(
    val id: Int,
    val status: String,
    val message: String,
    val meeting: MeetingEntity,
    )
