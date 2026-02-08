package ru.sicampus.bootcamp2026.data.network

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sicampus.bootcamp2026.data.model.CreateMeetingRequest
import ru.sicampus.bootcamp2026.data.model.ImageResponse
import ru.sicampus.bootcamp2026.data.model.InvitationDecisionRequest
import ru.sicampus.bootcamp2026.data.model.MeetingDto
import ru.sicampus.bootcamp2026.data.model.MeetingParticipantDto
import ru.sicampus.bootcamp2026.data.model.PageResponse
import ru.sicampus.bootcamp2026.data.model.UpdateUserRequest
import ru.sicampus.bootcamp2026.data.model.UserDto

interface AppApi {

    @GET("api/users/paginated")
    suspend fun getUsers(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PageResponse<UserDto>

    @POST("api/meetings")
    suspend fun createMeeting(
        @Header("Authorization") token: String,
        @Body req: CreateMeetingRequest
    ): MeetingDto

    @GET("api/meetings")
    suspend fun getMeetings(
        @Header("Authorization") token: String
    ): List<MeetingDto>

    @GET("api/meetings/{id}/participants")
    suspend fun getMeetingParticipants(
        @Header("Authorization") token: String,
        @Path("id") meetingId: Long
    ): List<MeetingParticipantDto>

    @GET("api/users/login")
    suspend fun getMe(@Header("Authorization") token: String): UserDto

    @PUT("api/users/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body req: UpdateUserRequest
    ): UserDto

    @GET("api/users/{userId}/meetings")
    suspend fun getInvites(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Query("status") status: String = "PENDING"
    ): List<MeetingDto>

    @PATCH("api/users/{userId}/meetings/{meetingId}/invitation")
    suspend fun decideInvitation(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Path("meetingId") meetingId: Long,
        @Body req: InvitationDecisionRequest
    ): InvitationDecisionRequest

    @GET("api/users/paginated")
    suspend fun getUsers(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 20,
        @Query("search") search: String?
    ): PageResponse<UserDto>

    @Multipart
    @POST("api/images/upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): ImageResponse

    @GET("api/users/{userId}/meetings")
    suspend fun getInvites(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Query("status") status: String = "PENDING",
        @Query("page") page: Int,
        @Query("size") size: Int = 10
    ): PageResponse<MeetingDto>
}