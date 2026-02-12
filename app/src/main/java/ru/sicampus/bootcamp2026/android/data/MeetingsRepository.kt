package ru.sicampus.bootcamp2026.android.data

import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import ru.sicampus.bootcamp2026.android.data.dto.MeetingsCountByDateDto
import ru.sicampus.bootcamp2026.android.data.dto.PageResponse
import ru.sicampus.bootcamp2026.android.data.source.MeetingsNetworkDataSource

class MeetingsRepository(
    private val network: MeetingsNetworkDataSource
) {
    suspend fun getMyMeetingsForDay(date: String, page: Int, size: Int):
            Result<PageResponse<MeetingResponse>> = network.getMyMeetingsForDay(date, page, size)

    suspend fun getMyMeetingsForWeek(start: String):
            Result<List<MeetingsCountByDateDto>> = network.getMyMeetingsForWeek(start)

    suspend fun getMyMeetingsForMonth(month: String):
            Result<List<MeetingsCountByDateDto>> = network.getMyMeetingsForMonth(month)
}
