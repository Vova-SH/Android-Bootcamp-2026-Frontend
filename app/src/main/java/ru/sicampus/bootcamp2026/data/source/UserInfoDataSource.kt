package ru.sicampus.bootcamp2026.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.UserDto
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

@Serializable
data class MeetingDto(
    val id: Long? = null,
    val title: String,
    val date: String,
    val time: String,
    val members: Int? = null,
    val confirmed: Boolean? = false
)

open class UserInfoDataSource {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    open suspend fun getAllUsers(): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(Network.HOST + "/api/user")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
                connectTimeout = 15000
                readTimeout = 15000
            }

            val code = conn.responseCode
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            conn.disconnect()

            if (code !in 200..299) error("Status: $code, body: $text")

            json.decodeFromString<List<UserDto>>(text)
        }
    }

    open suspend fun getUserById(id: Long): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(Network.HOST + "/api/user/" + id)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
                connectTimeout = 15000
                readTimeout = 15000
            }

            val code = conn.responseCode
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            conn.disconnect()

            if (code !in 200..299) error("Status: $code, body: $text")

            json.decodeFromString<UserDto>(text)
        }
    }

    open suspend fun registerUser(user: UserDto): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(Network.HOST + "/api/user/register")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
                connectTimeout = 15000
                readTimeout = 15000
            }

            val body = json.encodeToString(user)
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }

            val code = conn.responseCode
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            conn.disconnect()

            if (code !in 200..299) error("Status: $code, body: $text")

            json.decodeFromString<UserDto>(text)
        }
    }

    open suspend fun updateUser(id: Long, user: UserDto): Result<UserDto> = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(Network.HOST + "/api/user/" + id)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "PUT"
                doOutput = true
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
                connectTimeout = 15000
                readTimeout = 15000
            }

            val body = json.encodeToString(user)
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }

            val code = conn.responseCode
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            conn.disconnect()

            if (code !in 200..299) error("Status: $code, body: $text")

            json.decodeFromString<UserDto>(text)
        }
    }

    open suspend fun deleteUser(id: Long): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(Network.HOST + "/api/user/" + id)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "DELETE"
                setRequestProperty("Accept", "application/json")
                connectTimeout = 15000
                readTimeout = 15000
            }

            val code = conn.responseCode
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            conn.disconnect()

            if (code !in 200..299) error("Status: $code, body: $text")

            Unit
        }
    }

    open suspend fun getAllMeetings(): Result<List<MeetingDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(Network.HOST + "/api/meetings")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
                connectTimeout = 15000
                readTimeout = 15000
            }

            val code = conn.responseCode
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            conn.disconnect()

            if (code !in 200..299) error("Status: $code, body: $text")

            json.decodeFromString<List<MeetingDto>>(text)
        }
    }

    open suspend fun getMeetingsByDate(date: String): Result<List<MeetingDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(Network.HOST + "/api/meetings?date=$date")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
                connectTimeout = 15000
                readTimeout = 15000
            }

            val code = conn.responseCode
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            conn.disconnect()

            if (code !in 200..299) error("Status: $code, body: $text")

            json.decodeFromString<List<MeetingDto>>(text)
        }
    }
}