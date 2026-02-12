package ru.sicampus.bootcamp2026.ui.screen.auth

import android.os.Build
import androidx.annotation.RequiresApi
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.network.dto.EmployeesDto
import ru.sicampus.bootcamp2026.data.network.source.Network
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource.addAuthHeader

class AuthDataSource {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getUser(): Result<List<EmployeesDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/Employee") {
                addAuthHeader()
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}