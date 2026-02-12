package com.example.create_meet.data

import android.util.Log
import com.example.comon.Network
import com.example.comon.UserDto
import com.example.create_meet.data.dto.CreateMeetingDto
import com.example.create_meet.data.dto.MeetingResponse
import com.example.create_meet.data.dto.MeetingsResult
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


import javax.inject.Inject

class MeetingsNetworkDataSource @Inject constructor(
    private val network: Network
){

    suspend fun getSchedule(token: String): MeetingsResult = withContext(Dispatchers.IO) {
        try {
            val response = network.client.get("${network.HOST}/meetings/schedule") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            return@withContext if (response.status.isSuccess()) {
                val meetings = response.body<List<MeetingResponse>>()
                Log.d("USERS","${meetings}")

                MeetingsResult.Success(meetings)
            } else {
                MeetingsResult.Error("Ошибка получения расписания: ${response.status.value}")
            }
        } catch (e: Exception) {
            MeetingsResult.Error(e.message ?: "Ошибка сети")
        }
    }

    suspend fun getUsers(token: String): UsersResult = withContext(Dispatchers.IO) {
        try {
            val response = network.client.get("${network.HOST}/users") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }


            return@withContext if (response.status.isSuccess()) {
                val users = response.body<List<UserDto>>()
                Log.d("USERS","${users}")
                UsersResult.Success(users)
            } else {
                UsersResult.Error("Ошибка получения пользователей: ${response.status.value}")
            }
        } catch (e: Exception) {
            UsersResult.Error(e.message ?: "Ошибка сети")
        }
    }

    suspend fun createMeeting(
        token: String,
        request: CreateMeetingDto
    ): CreateMeetingResult = withContext(Dispatchers.IO) {
        try {
            val response = network.client.post("${network.HOST}/meetings") {
                header(HttpHeaders.Authorization, "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            return@withContext if (response.status.isSuccess()) {
                val meeting =
                    response.body<MeetingResponse>()
                CreateMeetingResult.Success(meeting)
            } else {
                CreateMeetingResult.Error("Ошибка создания встречи: ${response.status.value}")
            }
        } catch (e: Exception) {
            CreateMeetingResult.Error(e.message ?: "Ошибка сети")
        }
    }

    suspend fun getInvitations(token: String): InvitationsResult = withContext(Dispatchers.IO) {
        try {
            val response = network.client.get("${network.HOST}/meetings/invitations") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            return@withContext if (response.status.isSuccess()) {
                val invites = response.body<List<InvitationResponse>>()
                InvitationsResult.Success(invites)
            } else {
                InvitationsResult.Error("Ошибка получения приглашений: ${response.status.value}")
            }
        } catch (e: Exception) {
            InvitationsResult.Error(e.message ?: "Ошибка сети")
        }
    }


    suspend fun acceptInvitation(token: String, invitationId: String): InvitationResult = withContext(Dispatchers.IO) {
        try {
            Log.d("INVATATIONS", "$invitationId")
            val response = network.client.post("${network.HOST}/meetings/invitations/$invitationId/accept") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            return@withContext if (response.status.isSuccess()) {
                InvitationResult.Success
            } else {
                InvitationResult.Error("Ошибка подтверждения: ${response.status.value}")
            }
        } catch (e: Exception) {
            InvitationResult.Error(e.message ?: "Ошибка сети")
        }
    }

    suspend fun declineInvitation(token: String, invitationId: String): InvitationResult = withContext(Dispatchers.IO) {
        try {
            val response = network.client.post("${network.HOST}/meetings/invitations/$invitationId/decline") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            return@withContext if (response.status.isSuccess()) {
                InvitationResult.Success
            } else {
                InvitationResult.Error("Ошибка отклонения: ${response.status.value}")
            }
        } catch (e: Exception) {
            InvitationResult.Error(e.message ?: "Ошибка сети")
        }
    }

}


