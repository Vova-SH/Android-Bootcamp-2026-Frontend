package com.example.meet.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.accept
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.ContentType
import com.example.meet.data.dto.CreateMeetingDto
import com.example.meet.data.dto.MeetingDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MeetingDataSource {

    suspend fun getMeetingById(id: Int): Result<MeetingDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response = Network.client.get("/api/meetings/$id")

            if (response.status != HttpStatusCode.OK) {
                error("Failed to get meeting: ${response.status}")
            }

            response.body<MeetingDto>()
        }
    }

    suspend fun createMeeting(meeting: CreateMeetingDto): Result<MeetingDto> = withContext(Dispatchers.IO) {
        runCatching {
            val paths = listOf("/api/meetings", "/meetings", "/api/meetings/create")
            var lastError: String? = null
            for (path in paths) {
                val response = Network.client.post(path) {
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                    setBody(meeting)
                }
                if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                    return@runCatching response.body<MeetingDto>()
                } else {
                    val raw = try { response.body<String>() } catch (_: Exception) { "" }
                    lastError = "(${response.status}) ${raw.take(300)}"
                }
            }
            // д э б
            error("Ошибка создания встречи: ${lastError ?: "неизвестная ошибка"}")
        }
    }
}
