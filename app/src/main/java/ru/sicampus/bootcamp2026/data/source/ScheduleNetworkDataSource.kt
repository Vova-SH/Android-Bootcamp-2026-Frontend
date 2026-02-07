package ru.sicampus.bootcamp2026.data.source

import android.annotation.SuppressLint
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.sicampus.bootcamp2026.data.dto.MeetinCreateDTO
import ru.sicampus.bootcamp2026.data.dto.ScheduleEntryDTO
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScheduleNetworkDataSource {
    val _userId = MutableStateFlow<Long?>(null)
    val data = UsersInfoDataSource()

    suspend fun loadUserIDByEmail(email: String?) {
        data.getUserByEmail(email).onSuccess { user ->
            _userId.value = user.id.toLong()
        }.onFailure {
            _userId.value = 1L
        }
    }

    @SuppressLint("NewApi")
    suspend fun findMeetingsTitles(
    ): List<String> = withContext(Dispatchers.IO) {
        val token = AuthLocalDataSource.token ?: return@withContext emptyList()
        val userId = _userId.value ?: 1
        val startDate = LocalDate.now()
        val endDate = startDate.plusDays(7)

        val result = Network.client.get("${Network.HOST}/api/schedule") {
            header(HttpHeaders.Authorization, "Basic $token")
            header("X-User-Id", userId.toString())
            parameter("startDate", startDate.toString())
            parameter("endDate", endDate.toString())
        }

        return@withContext if (result.status == HttpStatusCode.OK) {
            try {
                @Serializable
                data class MeetingResponse(val topic: String, val dateTime: String)
                val meetings = Json.decodeFromString<List<MeetingResponse>>(result.bodyAsText())
                meetings.map { it.topic }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    @SuppressLint("NewApi")
    suspend fun findMeetingsDT(
    ): List<String> = withContext(Dispatchers.IO) {
        val token = AuthLocalDataSource.token ?: return@withContext emptyList()
        val userId = _userId.value ?: 1
        val startDate = LocalDate.now()
        val endDate = startDate.plusDays(7)

        val result = Network.client.get("${Network.HOST}/api/schedule") {
            header(HttpHeaders.Authorization, "Basic $token")
            header("X-User-Id", userId.toString())
            parameter("startDate", startDate.toString())
            parameter("endDate", endDate.toString())
        }

        return@withContext if (result.status == HttpStatusCode.OK) {
            try {
                @Serializable
                data class MeetingResponse(val topic: String, val dateTime: String)
                val meetings = Json.decodeFromString<List<MeetingResponse>>(result.bodyAsText())
                meetings.map { meeting ->
                    val startTime = LocalDateTime.parse(meeting.dateTime)
                    val endTime = startTime.plusHours(1)

                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                    val startFormatted = startTime.format(formatter)
                    val endFormatted = endTime.format(formatter)

                    "$startFormatted - $endFormatted"
                }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

}
