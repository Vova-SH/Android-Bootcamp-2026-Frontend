package com.teto.planner.data.remote.dto.user

import com.teto.planner.domain.model.common.Role
import com.teto.planner.domain.model.user.LoadStatus
import com.teto.planner.domain.model.user.UserMe
import com.teto.planner.domain.model.user.UserSummary
import kotlinx.serialization.Serializable

@Serializable
data class UserSummaryDto(
    val id: String,
    val login: String? = null,
    val name: String,
    val avatarUrl: String? = null,
    val busyHours: Int? = 0,
    val loadStatus: String? = "LOW",
    val telegramNick: String? = null,
    val bio: String? = null
)

@Serializable
data class UserMeDto(
    val id: String,
    val login: String,
    val name: String,
    val telegramNick: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val avatarContentType: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val roles: List<RoleDto>? = null
)

@Serializable
data class RoleDto(
    val id: String,
    val slug: String,
    val name: String
)

fun UserSummaryDto.toDomain() = UserSummary(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    busyHours = busyHours ?: 0,
    loadStatus = try {
        LoadStatus.valueOf(loadStatus ?: "LOW")
    } catch (e: Exception) {
        LoadStatus.LOW
    }
)


fun UserMeDto.toDomain() = UserMe(
    id = id,
    login = login,
    name = name,
    bio = bio,
    telegram = telegramNick,
    avatarUrl = avatarUrl,
    roles = roles?.map { it.toDomain() } ?: emptyList()
)

fun RoleDto.toDomain() = Role(
    id = id,
    slug = slug,
    name = name
)