package ru.sicampus.bootcamp2026.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sicampus.bootcamp2026.data.dto.InvitationDto
import ru.sicampus.bootcamp2026.data.dto.InvitationStatusDto
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto

interface BootcampApi {
    @POST("api/users/v1/register")
    suspend fun register(@Body dto: UserRegisterDto): UserDto

    @GET("api/users/v1/login")
    suspend fun login(): UserDto

    @GET("api/users/v1/all")
    suspend fun getAllUsers(): List<UserDto>

    @PUT("api/users/v1/me")
    suspend fun updateMe(@Body dto: UserDto): UserDto

    @DELETE("api/users/v1/me")
    suspend fun deleteMe(): Response<Unit>

    @GET("api/meetings/v1/{id}")
    suspend fun getMeetingById(@Path("id") id: Long): MeetingDto

    @POST("api/meetings/v1")
    suspend fun createMeeting(@Body dto: MeetingDto): MeetingDto

    @PUT("api/meetings/v1/{id}")
    suspend fun updateMeeting(@Path("id") id: Long, @Body dto: MeetingDto): MeetingDto

    @DELETE("api/meetings/v1/{id}")
    suspend fun deleteMeeting(@Path("id") id: Long): Response<Unit>


    @GET("api/schedule/v1/me/day")
    suspend fun getDaySchedule(
        @Query("date") dateIso: String
    ): List<MeetingDto>

    @GET("api/schedule/v1/me/week")
    suspend fun getWeekSchedule(): List<MeetingDto>

    @GET("api/schedule/v1/me/two-weeks")
    suspend fun getTwoWeeksSchedule(): List<MeetingDto>

    @GET("api/schedule/v1/me/month")
    suspend fun getMonthSchedule(): List<MeetingDto>


    @GET("api/invitations/v1/me")
    suspend fun getMyInvitations(): List<InvitationDto>

    @PATCH("api/invitations/v1/{invitationId}")
    suspend fun respondToInvitation(
        @Path("invitationId") invitationId: Long,
        @Query("status") status: InvitationStatusDto
    ): Response<Unit>

    @GET("api/invitations/v1/meeting/{meetingId}")
    suspend fun getMeetingInvitations(@Path("meetingId") meetingId: Long): List<InvitationDto>
}
