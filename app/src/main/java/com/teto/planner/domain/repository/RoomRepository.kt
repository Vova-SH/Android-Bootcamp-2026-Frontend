package com.teto.planner.domain.repository

import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.common.Room
import java.time.LocalDate

interface RoomRepository {
    suspend fun listRooms(page: Int = 0, size: Int = 50): Result<PagedList<Room>>

    suspend fun listAvailableRooms(
        date: LocalDate,
        startHour: Int,
        capacity: Int? = null
    ): Result<PagedList<Room>>
}