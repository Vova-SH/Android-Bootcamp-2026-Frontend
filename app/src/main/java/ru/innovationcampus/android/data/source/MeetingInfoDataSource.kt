package ru.innovationcampus.android.data.source

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.ByteString.Companion.decodeBase64
import okio.Utf8
import ru.innovationcampus.android.data.dto.PagingMeetingListDto
import kotlin.io.encoding.Base64

class MeetingInfoDataSource {
        suspend fun getMeeting(
            page: Int,
            size: Int
        ): Result<PagingMeetingListDto> = withContext(Dispatchers.IO) {
            runCatching {
                val token = AuthLocalDataSource.getToken().orEmpty();
                val bytes = Base64.decode(token, "Basic ".length)
                val username = String(bytes, Charsets.UTF_8).split(":")[0]
//                Log.d("MeetingInfoDataSource", username)
                val result = Network.client.get("${Network.HOST}/api/meetings/paginated/${username}") {
                    url {
                        parameter("page", page)
                        parameter("size", size)
                    }
                    addAuthHeader()
                }
                if (result.status != HttpStatusCode.OK) {
                    error("Status: ${result.status}")
                }
                result.body()
            }
        }

}