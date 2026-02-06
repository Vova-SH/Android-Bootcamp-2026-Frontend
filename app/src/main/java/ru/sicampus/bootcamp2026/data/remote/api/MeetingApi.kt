package ru.sicampus.bootcamp2026.data.remote.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sicampus.bootcamp2026.data.remote.dto.CreateMeetingRequest
import ru.sicampus.bootcamp2026.data.remote.dto.FreeTimeRequest
import ru.sicampus.bootcamp2026.data.remote.dto.FreeTimeResponse
import ru.sicampus.bootcamp2026.data.remote.dto.MeetingResponse
import ru.sicampus.bootcamp2026.data.remote.dto.PageResponse
import java.util.UUID

/**
 * API интерфейс для работы со встречами
 */
interface MeetingApi {

    @POST("api/v1/meetings")
    suspend fun createMeeting(
        @Body request: CreateMeetingRequest
    ): MeetingResponse

    @GET("api/v1/meetings/{meetingId}")
    suspend fun getMeetingById(
        @Path("meetingId") meetingId: UUID
    ): MeetingResponse

    @GET("api/v1/meetings")
    suspend fun getUserMeetings(
        @Query("status") status: String? = null,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PageResponse<MeetingResponse>

    @PUT("api/v1/meetings/{meetingId}/cancel")
    suspend fun cancelMeeting(
        @Path("meetingId") meetingId: UUID
    ): MeetingResponse

    @DELETE("api/v1/meetings/{meetingId}")
    suspend fun deleteMeeting(
        @Path("meetingId") meetingId: UUID
    )

    @POST("api/v1/meetings/freeTime")
    suspend fun getFreeTime(
        @Body request: FreeTimeRequest
    ): FreeTimeResponse
}

