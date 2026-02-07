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