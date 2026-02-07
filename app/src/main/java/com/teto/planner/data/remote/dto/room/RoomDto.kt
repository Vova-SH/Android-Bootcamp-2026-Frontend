package com.teto.planner.data.remote.dto.room

import com.teto.planner.domain.model.common.Room
import kotlinx.serialization.Serializable

@Serializable
data class RoomDto(
    val id: String,
    val name: String,
    val capacity: Int
)

fun RoomDto.toDomain() = Room(
    id = id,
    name = name,
    capacity = capacity
)