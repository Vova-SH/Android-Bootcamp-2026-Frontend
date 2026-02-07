package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.Network
import ru.sicampus.bootcamp2026.data.dto.PagingEmployeeListDTO

class EmployeeSearchDataSource {
    suspend fun searchEmployees(query: String?, page: Int, size: Int): Result<PagingEmployeeListDTO> = withContext(Dispatchers.IO){
        runCatching {
            val result = Network.client.get("api/employee/all-paginated"){
                url{
                    if (!query.isNullOrBlank()) {
                        parameter("search", query)

                    }
                    parameter("page", page)
                    parameter("size", size)
                }
            }
            if (result.status != HttpStatusCode.OK){
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}