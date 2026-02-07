package ru.sicampus.bootcamp2026.data.source

import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.sicampus.bootcamp2026.data.dto.MeetinCreateDTO
import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.MeetingInfoViewModel

class MeetingCreateNetDataSource {

    val _userId = MutableStateFlow<Long?>(null)
    val data = UsersInfoDataSource()

    suspend fun loadUserIDByEmail(email: String?) {
        data.getUserByEmail(email).onSuccess { user ->
            _userId.value = user.id.toLong()
        }.onFailure {
            _userId.value = 1L
        }
    }

    suspend fun createMeeting(
        meeting: MeetinCreateDTO,
        ): Boolean = withContext(Dispatchers.IO){
        val token = AuthLocalDataSource.token ?: error("Not authorized")

        val userId = _userId.value ?: 1

        val result = Network.client.post("${Network.HOST}/api/meetings") {
            header(HttpHeaders.Authorization, "Basic $token")
            header("X-User-Id", userId.toLong())
            header(HttpHeaders.ContentType, "application/json")
            setBody(Json.encodeToString(meeting))
        }

        result.status == HttpStatusCode.OK
    }
}