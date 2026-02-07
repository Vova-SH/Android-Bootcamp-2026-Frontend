package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.Network
import ru.sicampus.bootcamp2026.data.dto.EmployeeRequestDTO

class AuthNetworkDataSource {
    suspend fun login(): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.post("api/employee/login")
            result.status == HttpStatusCode.OK
        }
    }
    suspend fun register(employeeRequestDTO: EmployeeRequestDTO): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.post("api/employee/register"){
                setBody(employeeRequestDTO)
                headers.remove(HttpHeaders.Authorization)
            }
            if (result.status.value != 200) {
                error("Error: ${result.status.value}")
            }
        }
    }
}