package ru.sicampus.bootcamp2026.data.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("position") val position: String?,
    @SerializedName(value = "email", alternate = ["login"])
    val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?
)


data class PageResponse<T>(
    @SerializedName("content") val content: List<T>,
    @SerializedName("last") val last: Boolean,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int
)

data class CreateMeetingRequest(
    @SerializedName("title") val title: String,
    @SerializedName("startsAt") val startsAt: String,
    @SerializedName("endsAt") val endsAt: String,
    @SerializedName("description") val description: String?,
    @SerializedName("invitedUserIds") val invitedUserIds: List<Long>
)