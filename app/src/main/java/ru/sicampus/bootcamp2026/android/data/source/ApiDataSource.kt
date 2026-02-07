package ru.sicampus.bootcamp2026.android.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.android.data.dto.InvitationDTO
import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import ru.sicampus.bootcamp2026.android.data.dto.UserDTO

class ApiDataSource {

    suspend fun getUsers(): Result<List<UserDTO>> =
        safeGet("${Network.BASE_URL}/users")

    suspend fun getMeetings(): Result<List<MeetingResponse>> =
        safeGet("${Network.BASE_URL}/meetings")

    suspend fun getInvitations(): Result<List<InvitationDTO>> =
        safeGet("${Network.BASE_URL}/invitations")

    private suspend inline fun <reified T> safeGet(url: String): Result<T> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = Network.client.get(url)
                if (response.status != HttpStatusCode.OK) {
                    error("Status: ${response.status}")
                }
                response.body<T>()
            }
        }
}
