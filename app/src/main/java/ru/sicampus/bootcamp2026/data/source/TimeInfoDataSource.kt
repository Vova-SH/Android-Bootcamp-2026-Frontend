package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.TimeSlotDto

class TimeInfoDataSource {

    suspend fun getSlots(
        date: String,
    ): Result<List<TimeSlotDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/meetings/slots"){
                url {
                    parameter("date", date)
                }
                addAuthHeader()
            }
            if (result.status != HttpStatusCode.OK){
                error("Статус: ${result.status}")
            }
            result.body<List<TimeSlotDto>>()
        }
    }
}