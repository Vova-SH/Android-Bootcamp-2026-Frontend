package com.example.meet.data.source

import android.os.Build
import android.util.Log
import com.example.meet.BuildConfig
import com.example.meet.data.dto.InvitationDto
import com.example.meet.data.dto.InvitationPageDto
import com.example.meet.data.dto.JwtResponse
import com.example.meet.data.dto.LoginRequest
import com.example.meet.data.dto.MeetingDto
import com.example.meet.data.dto.NotificationDto
import com.example.meet.data.dto.NotificationPageDto
import com.example.meet.data.dto.RegisterRequest
import com.example.meet.data.dto.UserDto
import com.example.meet.data.source.AuthPrefs.getToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.timeout
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@ExperimentalSerializationApi
object Network {

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = false
    }

    private fun isEmulator(): Boolean {
        val fp = Build.FINGERPRINT.lowercase()
        val model = Build.MODEL.lowercase()
        val product = Build.PRODUCT.lowercase()
        return fp.contains("generic") || model.contains("emulator") || product.contains("sdk")
    }

    private const val HOST_EMULATOR = "http://10.0.2.2:8080"
    private const val HOST_CONFIG: String = BuildConfig.BASE_URL
    var HOST: String =
        if (isEmulator() && (HOST_CONFIG.contains("localhost") || HOST_CONFIG.contains("127.0.0.1"))) {
            HOST_EMULATOR
        } else {
            HOST_CONFIG
        }

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    encodeDefaults = false
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 10000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 10000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KTOR", message)
                }
            }
        }

        defaultRequest {
            url(HOST)
            contentType(ContentType.Application.Json)
            _authToken?.let { token ->
                header("Authorization", "Bearer $token")
            }
        }
    }


    private var _authToken: String? = null
    private var _currentUser: UserDto? = null
    val currentUser: UserDto? get() = _currentUser
    val currentUserId: Long? get() = _currentUser?.id
    val isLoggedIn: Boolean get() = _authToken != null && _currentUser != null

    fun logout() {
        _authToken = null
        _currentUser = null
        AuthPrefs.clear()
    }

    suspend fun restoreSession(): Boolean {
        val token = getToken()
        val user = AuthPrefs.getUserData()

        if (token != null && user != null) {
            _authToken = token
            _currentUser = user

            //Проверка валидности
            return try {
                val response: HttpResponse = client.get("/api/users/${user.id}") {
                    timeout {
                        requestTimeoutMillis = 5000
                        connectTimeoutMillis = 5000
                        socketTimeoutMillis = 5000
                    }
                }
                response.status == HttpStatusCode.OK
            } catch (_: Exception) {
                logout()
                false
            }
        }
        return false
    }

    suspend fun pingServer(): Boolean = runCatching {
        val response: HttpResponse = client.get("/actuator/health") {
            timeout {
                requestTimeoutMillis = 5000
                connectTimeoutMillis = 5000
                socketTimeoutMillis = 5000
            }
        }
        response.status == HttpStatusCode.OK
    }.getOrDefault(false)

    private fun <T> paginate(list: List<T>, page: Int, size: Int): List<T> {
        if (page < 0 || size <= 0) return emptyList()
        val from = page * size
        if (from >= list.size) return emptyList()
        return list.drop(from).take(size)
    }
    suspend fun getUsers(
        page: Int? = null,
        size: Int? = null,
        clientSidePaging: Boolean = true,
    ): List<UserDto> {
        println("DEBUG: Делаем запрос на /api/users")
        val users: List<UserDto> = client.get("/api/users") {
            timeout {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }
        }.body()
        println("DEBUG: Получено ${users.size} пользователей")
        return if (clientSidePaging && page != null && size != null) paginate(users, page, size) else users
    }

    suspend fun getUserById(id: Long): UserDto =
        client.get("/api/users/$id") {
            timeout {
                requestTimeoutMillis = 8000
                connectTimeoutMillis = 8000
                socketTimeoutMillis = 8000
            }
        }.body()

    suspend fun createUser(
        email: String,
        password: String,
        fullName: String,
        position: String? = null,
        department: String? = null,
    ): UserDto {
        val body = UserDto(
            email = email,
            passwordHash = password,
            fullName = fullName,
            position = position,
            department = department,
            role = "USER",
            isActive = true
        )
        return client.post("/api/users") {
            setBody(body)
            timeout {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }
        }.body()
    }

    suspend fun login(email: String, password: String): UserDto {
        val response: HttpResponse = client.post("/api/auth/login") {
            setBody(LoginRequest(email = email, password = password))
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            timeout {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                try {
                    val jwtResponse: JwtResponse = response.body()
                    _authToken = jwtResponse.token
                    _currentUser = UserDto(
                        id = jwtResponse.id,
                        email = jwtResponse.email,
                        fullName = jwtResponse.fullName,
                        passwordHash = null
                    )

                    _currentUser?.let {
                        AuthPrefs.saveLoginData(jwtResponse.token, it)
                    }

                    return _currentUser!!
                } catch (_: Exception) {
                    val errorBody = response.body<String>()
                    throw Exception("Ошибка формата ответа: $errorBody")
                }
            }
            HttpStatusCode.Forbidden -> {
                throw Exception("Неверный email или пароль")
            }
            HttpStatusCode.Unauthorized -> {
                throw Exception("Неверный email или пароль")
            }
            else -> {
                val errorBody = try { response.body<String>() } catch (_: Exception) { "Неизвестная ошибка" }
                throw Exception("Ошибка сервера (${response.status}): $errorBody")
            }
        }
    }

    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        position: String? = null,
        department: String? = null
    ): UserDto {
        val requestBody = RegisterRequest(
            email = email,
            password = password,
            fullName = fullName,
            position = position,
            department = department
        )
        val candidates = listOf(
            HOST_CONFIG,
            HOST_EMULATOR,
            "http://127.0.0.1:8080",
            "http://localhost:8080"
        ).distinct()
        var lastError: Exception? = null
        for (base in candidates) {
            try {
                val resp = client.post("$base/api/auth/register") {
                    setBody(requestBody)
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                    timeout {
                        requestTimeoutMillis = 10000
                        connectTimeoutMillis = 10000
                        socketTimeoutMillis = 10000
                    }
                }
                if (resp.status == HttpStatusCode.OK || resp.status == HttpStatusCode.Created) {
                    HOST = base
                    return login(email, password)
                } else {
                    val reason = try { resp.body<String>() } catch (_: Exception) { "" }
                    lastError = Exception(
                        when {
                            resp.status == HttpStatusCode.BadRequest && reason.contains("already in use", ignoreCase = true) ->
                                "Email уже используется"
                            else -> "Ошибка регистрации (${resp.status}): ${reason.ifBlank { "Неизвестная ошибка" }}"
                        }
                    )
                }
            } catch (e: Exception) {
                lastError = e
            }
        }
        throw lastError ?: Exception("Сервер недоступен для регистрации")
    }

    suspend fun getMeetings(): List<MeetingDto> {
        return client.get("/api/meetings") {
            timeout {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }
        }.body()
    }

    suspend fun getInvitations(
        page: Int? = null,
        size: Int? = null,
        clientSidePaging: Boolean = true,
    ): List<InvitationDto> {
        val resp = client.get("/api/invitations") {
            timeout {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }
            accept(ContentType.Application.Json)
        }
        val raw = try { resp.bodyAsText() } catch (_: Exception) { "" }
        val items: List<InvitationDto> = try {
            json.decodeFromString<List<InvitationDto>>(raw)
        } catch (_: Exception) {
            try {
                val pageObj = json.decodeFromString<InvitationPageDto>(raw)
                pageObj.content
            } catch (_: Exception) {
                throw Exception("Ошибка формата ответа: $raw")
            }
        }
        return if (clientSidePaging && page != null && size != null) paginate(items, page, size) else items
    }

    suspend fun getNotifications(
        userId: Long,
        page: Int? = null,
        size: Int? = null,
        clientSidePaging: Boolean = true,
    ): List<NotificationDto> {
        val resp = client.get("/api/notifications") {
            timeout {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }
            accept(ContentType.Application.Json)
        }
        val raw = try { resp.bodyAsText() } catch (_: Exception) { "" }
        val items: List<NotificationDto> = try {
            json.decodeFromString<List<NotificationDto>>(raw)
        } catch (_: Exception) {
            try {
                val pageObj = json.decodeFromString<NotificationPageDto>(raw)
                pageObj.content
            } catch (_: Exception) {
                throw Exception("Ошибка формата ответа: $raw")
            }
        }

        val filteredItems = items.filter { it.userId == userId }
        return if (clientSidePaging && page != null && size != null) paginate(filteredItems, page, size) else filteredItems
    }

    suspend fun getInvitations(): List<InvitationDto> {
        return client.get("/api/invitations").body()
    }

    suspend fun markNotificationAsRead(notificationId: Long) {
        val response = client.put("$HOST/api/notifications/$notificationId/read") {
            headers {
                append("Authorization", "Bearer ${getToken()}")
            }
        }

        if (!response.status.isSuccess()) {
            throw Exception("Не удалось отметить уведомление как прочитанное")
        }
    }
}
