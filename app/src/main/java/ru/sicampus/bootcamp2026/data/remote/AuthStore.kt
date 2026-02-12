package ru.sicampus.bootcamp2026.data.remote

import android.util.Base64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.sicampus.bootcamp2026.data.dto.UserDto
object AuthStore {

    data class Credentials(
        val login: String,
        val password: String
    ) {
        fun basicAuthHeaderValue(): String {
            val raw = "$login:$password"
            val encoded = Base64.encodeToString(raw.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
            return "Basic $encoded"
        }
    }

    private val _credentials = MutableStateFlow<Credentials?>(null)
    val credentials: StateFlow<Credentials?> = _credentials.asStateFlow()

    private val _user = MutableStateFlow<UserDto?>(null)
    val user: StateFlow<UserDto?> = _user.asStateFlow()

    fun setCredentials(creds: Credentials?) {
        _credentials.value = creds
    }

    fun setUser(user: UserDto?) {
        _user.value = user
    }

    fun setSession(creds: Credentials, user: UserDto) {
        _credentials.value = creds
        _user.value = user
    }

    fun clear() {
        _credentials.value = null
        _user.value = null
    }

    fun currentAuthHeader(): String? = _credentials.value?.basicAuthHeaderValue()
}
