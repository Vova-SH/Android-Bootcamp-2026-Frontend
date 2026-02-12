package ru.sicampus.bootcamp2026.data.source

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.Network
import ru.sicampus.bootcamp2026.data.dto.EmployeeDTO
import ru.sicampus.bootcamp2026.data.dto.UpdateProfileRequestDTO

class ProfileDataSource {

    suspend fun getProfile(): Result<EmployeeDTO> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.post("api/employee/login")
            if (result.status != HttpStatusCode.OK) {
                error("Error: ${result.status}")
            }
            result.body<EmployeeDTO>()
        }
    }

    suspend fun editProfile(updateProfileRequestDTO: UpdateProfileRequestDTO): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.patch("api/employee") {
                setBody(updateProfileRequestDTO)
            }
            if (result.status != HttpStatusCode.OK) {
                Log.d("KTOR", "Response status: $updateProfileRequestDTO")
                error("Error: ${result.status}")
            }

        }
    }
}