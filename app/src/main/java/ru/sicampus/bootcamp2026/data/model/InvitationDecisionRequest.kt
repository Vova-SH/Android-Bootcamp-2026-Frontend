package ru.sicampus.bootcamp2026.data.model

import com.google.gson.annotations.SerializedName

data class InvitationDecisionRequest(
    @SerializedName("status") val status: String
)