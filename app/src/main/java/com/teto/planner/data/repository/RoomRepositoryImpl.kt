package com.teto.planner.data.repository

import com.teto.planner.data.remote.dto.RoomsPageDto
import com.teto.planner.data.remote.dto.room.toDomain
import com.teto.planner.data.remote.dto.toDomain
import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.common.Room
import com.teto.planner.domain.repository.RoomRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.LocalDate
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : RoomRepository {

    override suspend fun listRooms(page: Int, size: Int): Result<PagedList<Room>> = runCatching {
        val response = client.get("api/rooms") {
            parameter("page", page)
            parameter("size", size)
        }.body<RoomsPageDto>()

        PagedList(
            items = response.items.map { it.toDomain() },
            meta = response.meta?.toDomain() ?: PageMeta(page, size, 0)
        )
    }

    override suspend fun listAvailableRooms(
        date: LocalDate,
        startHour: Int,
        capacity: Int?
    ): Result<PagedList<Room>> = runCatching {
        val response = client.get("api/rooms/available") {
            parameter("meetingDate", date.toString())
            parameter("startHour", startHour)
            if (capacity != null) parameter("capacity", capacity)
            parameter("page", 0)
            parameter("size", 100)
        }.body<RoomsPageDto>()

        PagedList(
            items = response.items.map { it.toDomain() },
            meta = response.meta?.toDomain() ?: PageMeta(0, 100, response.items.size.toLong())
        )
    }
}