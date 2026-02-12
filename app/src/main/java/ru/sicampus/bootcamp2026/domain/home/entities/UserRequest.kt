package ru.sicampus.bootcamp2026.domain.home.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class UserRequest (
    @SerialName("id")
    val id : Int,
    @SerialName("email")
    val email: String,
    @SerialName("fullName")
    val fullName: String
)