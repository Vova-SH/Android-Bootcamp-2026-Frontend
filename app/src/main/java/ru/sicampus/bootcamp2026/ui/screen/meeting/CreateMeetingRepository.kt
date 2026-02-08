package ru.sicampus.bootcamp2026.screen.meetings


import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.network.source.Network
import java.time.LocalDateTime

class CreateMeetingRepository {
    suspend fun createMeeting(
        name: String,
        start_time: LocalDateTime,
        end_time: LocalDateTime
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val response = Network.client.post("${Network.HOST}/api/Booking/createdBo") {
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "name" to name,
                    "start_time" to start_time.toString(),
                    "end_time" to end_time.toString()
                ))
            }

            if (response.status != HttpStatusCode.OK) {
                throw Exception("Ошибка сервера: ${response.status}")
            }
        }
    }
}