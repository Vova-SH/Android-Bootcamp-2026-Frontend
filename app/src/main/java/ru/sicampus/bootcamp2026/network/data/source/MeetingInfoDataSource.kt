package ru.sicampus.bootcamp2026.network.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.network.data.dto.MeetingDto

class MeetingInfoDataSource {
    suspend fun getMeeting(): Result<List<MeetingDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/meeting")
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}