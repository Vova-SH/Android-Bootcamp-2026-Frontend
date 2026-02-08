package ru.sicampus.bootcamp2026.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Приглашение (часть отвечающая за отправителя)
 *
 * Другая часть: [InvitedDto]
 */
@Serializable
data class InvitationsDto(
    @SerialName("id")
    val id: Long?,
    @SerialName("Booking_id")
    val bookingId: Long?,
    @SerialName("Employee_id")
    val employeeId: Long?
)

/**
 * Приглашение (часть отвечающая за получателя)
 *
 * Другая часть: [InvitationsDto]
 */
@Serializable
data class InvitedDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("Employee_id")
    val employeeId: Long? = null,
    @SerialName("invitations_id")
    val invitationsId: Long? = null,
    @SerialName("Approval")
    val approval: Boolean? = null
)