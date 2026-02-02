package ru.sicampus.bootcamp2026.data.model

import com.google.gson.annotations.SerializedName

data class MeetingDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("color_hex") val colorHex: String? = null
)