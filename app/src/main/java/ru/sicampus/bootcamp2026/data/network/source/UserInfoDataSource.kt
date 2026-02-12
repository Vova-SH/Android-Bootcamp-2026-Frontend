package ru.sicampus.bootcamp2026.data.network.source

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.sicampus.bootcamp2026.data.network.dto.EmployeeDto
import ru.sicampus.bootcamp2026.data.network.dto.EmployeesDto
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import kotlin.runCatching

class UserInfoDataSource {
    suspend fun getUsers() : Result<List<EmployeeDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/Employee/Employees")
            Log.d("raw response", result.bodyAsText())
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body<EmployeesDto>().employees ?: listOf()
        }
    }

    suspend fun getUser(fio: String) : Result<EmployeesDto> = withContext(Dispatchers.IO) { // TODO заменить в дальнейшом на более подходящий метод, когда он появится на сервере
        runCatching {
            val result = Network.client.post("${Network.HOST}/api/employee") {
                contentType(ContentType.Application.Json)
                setBody(""" {
                        "last_name" : ${fio.split(" ")[0]}
                        "name":  ${fio.split(" ")[1]},
                        "father_name": ${fio.split(" ")[2]}"
                } """.trimIndent())
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }

    suspend fun searchUsers(search: String): Result<List<EmployeeDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.post("${Network.HOST}/api/Employee/Employee") {
                contentType(ContentType.Application.Json)
                val fio = """ {
                        "last_name" : "${search.split(" ")[0]}",
                        "name": "${search.split(" ")[1]}",
                        "father_name": "${search.split(" ")[2]}"
                    } """.trimIndent()
                Log.d("search", fio)
                setBody(fio)
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body<EmployeesDto>().employees ?: listOf()
        }
    }

    suspend fun getAuthUser() : Result<EmployeeDto> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/Employee/getYou")
            Log.d("raw response", result.bodyAsText())
            if (result.status != HttpStatusCode.OK) {
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}