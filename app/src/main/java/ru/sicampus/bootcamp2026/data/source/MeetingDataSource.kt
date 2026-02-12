package ru.sicampus.bootcamp2026.data.source

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.Network
import ru.sicampus.bootcamp2026.data.dto.CreateInvitationRequestDTO
import ru.sicampus.bootcamp2026.data.dto.CreateMeetingRequestDTO
import ru.sicampus.bootcamp2026.data.dto.MeetingResponseDTO

class MeetingDataSource {
    suspend fun createMeeting(createMeetingRequestDTO: CreateMeetingRequestDTO): Result<MeetingResponseDTO> = withContext(Dispatchers.IO){
        Log.d("KTOR", "createMeeting: $createMeetingRequestDTO")
        runCatching {
            val result = Network.client.post("api/meeting"){
                setBody(createMeetingRequestDTO)
            }
            if (result.status != HttpStatusCode.Created){
                error("Status: ${result.status}")
            }
            result.body<MeetingResponseDTO>()
        }
    }
    suspend fun createInvitation(createInvitationRequestDTO: CreateInvitationRequestDTO): Result<Unit> = withContext(Dispatchers.IO){
        runCatching {
            val result = Network.client.post("api/invitation/batch"){
                setBody(createInvitationRequestDTO)
            }
            if (result.status != HttpStatusCode.Created){

                error("Status: ${result.status}")
            }
        }
    }
}