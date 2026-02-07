package ru.sicampus.bootcamp2026.data.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import java.time.LocalDateTime

data class InvitationDTO(
    @SerialName("invitationId")
    val invitationId: String,
    @SerialName("topic")
    val topic: String,
    @SerialName("dateTime")
    val dateTime: String,
    @SerialName("organizerName")
    val organizerName: String
){
    @SuppressLint("NewApi")
    fun parseDateTime(): LocalDateTime {
        return LocalDateTime.parse(dateTime)
    }
}

