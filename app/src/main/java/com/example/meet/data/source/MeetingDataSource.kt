package com.example.meet.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
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

    suspend fun getMeetings(userId: Int? = null): Result<List<MeetingDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val meetings = Network.getMeetings()
            if (userId != null) {
                //фильтр по роли юзера
                meetings.filter {
                    it.organizerId == userId.toLong() ||
                            it.participantIds?.contains(userId.toLong()) == true
                }
            } else {
                meetings
            }
        }
    }

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
            val response = Network.client.post("/api/meetings") {
                contentType(ContentType.Application.Json)
                setBody(meeting)
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> response.body<MeetingDto>()
                else -> error("Failed to create meeting: ${response.status}")
            }
        }
    }
}