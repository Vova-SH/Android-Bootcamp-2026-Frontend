package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.ProfileUpdateDTO
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource.setToken

class ProfileNetworkDataSource {
    val _userId = MutableStateFlow<Int?>(null)
    val data = UsersInfoDataSource()

    suspend fun loadUserIDByEmail(email: String) {
        data.getUserByEmail(email).onSuccess { user ->
            _userId.value = user.id
        }
    }


    suspend fun updateProfile(
        _userId: Int?,
        updateData: ProfileUpdateDTO,
        currentPassword: String,
        currentEmail: String?
    ): Boolean = withContext(Dispatchers.IO){
        runCatching {
            val token = setToken(currentEmail, currentPassword)

            val result = Network.client.put("${Network.HOST}/api/users/$_userId"){
                header(HttpHeaders.Authorization, "Basic $token")
                header(HttpHeaders.ContentType, "application/json")
                setBody(updateData)
            }
            if (result.status == HttpStatusCode.OK && updateData.email != null) {
                AuthLocalDataSource.setToken(updateData.email, currentPassword)
            }
            result.status == HttpStatusCode.OK
        }.getOrElse { false }
    }


}