package com.teto.planner.data.remote.dto

import com.teto.planner.data.remote.dto.meeting.InvitationDto
import com.teto.planner.data.remote.dto.meeting.MeetingDto
import com.teto.planner.data.remote.dto.room.RoomDto
import com.teto.planner.data.remote.dto.user.UserSummaryDto
import com.teto.planner.domain.model.common.PageMeta
import kotlinx.serialization.Serializable

@Serializable
data class PageMetaDto(
    val page: Int,
    val size: Int,
    val total: Long,
    val totalPages: Int? = null,
    val hasNext: Boolean? = null,
    val hasPrev: Boolean? = null
)

@Serializable
data class MeetingsPageDto(
    val items: List<MeetingDto>,
    val meta: PageMetaDto? = null
)

@Serializable
data class UsersPageDto(
    val items: List<UserSummaryDto>,
    val meta: PageMetaDto? = null
)

@Serializable
data class RoomsPageDto(
    val items: List<RoomDto>,
    val meta: PageMetaDto? = null
)

@Serializable
data class InvitationsPageDto(
    val items: List<InvitationDto>,
    val meta: PageMetaDto? = null
)


fun PageMetaDto.toDomain() = PageMeta(
    page = page,
    size = size,
    total = total,
    totalPages = totalPages ?: 0,
    hasNext = hasNext ?: false,
    hasPrev = hasPrev ?: false
)