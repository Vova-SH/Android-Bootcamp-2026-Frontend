package ru.sicampus.bootcamp2026.data.network.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.network.dto.BookingsDto


/**
 * Источник данных для встреч (реализация через API)
 */
class MeetingDataSource {
    suspend fun getAllBookings(): Result<BookingsDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/Invited/getInvited") {
                addAuthHeader()
//                contentType(ContentType.Application.Json)
//                setBody(""" {
//                    "date" : "$date"
//                } """.trimIndent())
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body<BookingsDto>()
        }
    }
}
