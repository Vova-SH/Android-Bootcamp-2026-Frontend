package com.teto.planner.domain.model.user

import com.teto.planner.domain.model.common.Role

data class UserMe(
    val id: String,
    val login: String,
    val name: String,
    val bio: String?,
    val telegram: String?,
    val avatarUrl: String?,
    val roles: List<Role>,
    val updatedAt: String?
)

data class UserSummary(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val telegram: String? = null,
    val bio: String? = null,
    val busyHours: Int = 0,
    val loadStatus: LoadStatus = LoadStatus.LOW,
    val updatedAt: String? = null
)

enum class LoadStatus { LOW, MEDIUM, HIGH }

enum class ParticipantRole { ORGANIZER, ATTENDEE }

enum class ParticipantStatus { PENDING, ACCEPTED, DECLINED }