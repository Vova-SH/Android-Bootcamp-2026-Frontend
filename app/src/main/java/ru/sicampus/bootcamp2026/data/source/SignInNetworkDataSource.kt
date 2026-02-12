package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.UserDto

class SignInNetworkDataSource {
    suspend fun checkSignIn(): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/users/login") {
                addSignInHeader()
            }
            cacheResult = result
            result.status == HttpStatusCode.OK
        }
    }
    suspend fun getSignedUser(): Result<List<UserDto>> = withContext(Dispatchers.IO){
        runCatching { cacheResult?.body() ?: error("List is null") }
    }
}

suspend fun HttpMessageBuilder.addSignInHeader() {
    val token = SignInLocalDataSource.getToken() ?: return
    header(HttpHeaders.Authorization, token)
}

private var cacheResult: HttpResponse? = null