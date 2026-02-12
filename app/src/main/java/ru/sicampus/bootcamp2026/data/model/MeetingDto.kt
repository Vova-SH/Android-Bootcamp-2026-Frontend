package ru.sicampus.bootcamp2026.data.model

import com.google.gson.annotations.SerializedName

data class MeetingDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("startsAt") val startsAt: String?,
    @SerializedName("endsAt") val endsAt: String?,
    @SerializedName("colorHex") val colorHex: String?,
    @SerializedName("description") val description: String? = null
)

/** Участник встречи: данные пользователя + статус приглашения (ACCEPTED, PENDING, REJECTED) */
data class MeetingParticipantDto(
    @SerializedName("id") val id: Long,
    @SerializedName("position") val position: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("invitationStatus") val invitationStatus: String?
)