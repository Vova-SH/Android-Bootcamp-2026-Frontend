package ru.sicampus.bootcamp2026.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.sicampus.bootcamp2026.data.dto.UserDto
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@Serializable
data class MeetingDto(
    val id: Long? = null,
    val title: String,
    val description: String,
    val organizerId: Long,
    val startTime: String,
    val endTime: String,
    val createdAt: String
)

open class UserInfoDataSource {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    open suspend fun login(username: String, password: String): Result<UserDto> =
        withContext(Dispatchers.IO) {
            runCatching {
                val url = URL(Network.HOST + "/api/person/login")

                val conn = url.openConnection() as HttpURLConnection
                val auth = "$username:$password"
                val encoded = android.util.Base64.encodeToString(
                    auth.toByteArray(),
                    android.util.Base64.NO_WRAP
                )

                conn.apply {
                    requestMethod = "GET"
                    setRequestProperty("Authorization", "Basic $encoded")
                    setRequestProperty("Accept", "application/json")
                    connectTimeout = 5000
                    readTimeout = 5000
                }

                val code = conn.responseCode
                val text = if (code in 200..299) {
                    conn.inputStream.bufferedReader().use { it.readText() }
                } else {
                    conn.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                }
                conn.disconnect()

                if (code != 200) error("HTTP $code: $text")

                json.decodeFromString<UserDto>(text)
            }
        }

    open suspend fun getAllUsers(): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL(Network.HOST + "/api/person")
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
            val url = URL(Network.HOST + "/api/person/" + id)
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
            val url = URL(Network.HOST + "/api/person/register")
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

    open suspend fun updateUser(id: Long, user: UserDto): Result<UserDto> =
        withContext(Dispatchers.IO) {
            runCatching {
                val url = URL(Network.HOST + "/api/person/" + id)
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
            val url = URL(Network.HOST + "/api/person/" + id)
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

    open suspend fun getMeetingsByDate(date: String): Result<List<MeetingDto>> =
        withContext(Dispatchers.IO) {
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