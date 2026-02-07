package com.teto.planner.domain.model.user

import com.teto.planner.domain.model.common.Role

data class User(
    val id: String,
    val login: String,
    val name: String,
    val bio: String?,
    val telegram: String?,
    val avatarUrl: String?
)

data class UserMe(
    val id: String,
    val login: String,
    val name: String,
    val bio: String?,
    val telegram: String?,
    val avatarUrl: String?,
    val roles: List<Role>
)

data class UserSummary(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val busyHours: Int = 0,
    val loadStatus: LoadStatus = LoadStatus.LOW
)

enum class LoadStatus { LOW, MEDIUM, HIGH }

enum class ParticipantRole { ORGANIZER, ATTENDEE }

enum class ParticipantStatus { PENDING, ACCEPTED, DECLINED }