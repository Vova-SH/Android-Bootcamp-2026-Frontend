package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import ru.sicampus.bootcamp2026.data.dto.InvitationCreateDto
import ru.sicampus.bootcamp2026.data.dto.InvitationDto

class InvitationService {
    private val client = Network.client

    suspend fun getAllInvitations(): List<InvitationDto> {
        return client.get("/api/invitation/").body()
    }

    suspend fun sendInvitation(dto: InvitationCreateDto): InvitationDto {
        val response = client.post("/api/invitation/send") {
            setBody(dto)
        }

        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorText = response.bodyAsText()
            throw Exception(errorText.ifBlank { "Ошибка отправки: ${response.status.value}" })
        }
    }

    suspend fun acceptInvitation(id: Long) {
        val response = client.patch("/api/invitation/$id/accept")
        if (!response.status.isSuccess()) {
            val errorText = response.bodyAsText()
            throw Exception(errorText.ifBlank { "Ошибка: ${response.status.value}" })
        }
    }

    suspend fun rejectInvitation(id: Long) {
        val response = client.patch("/api/invitation/$id/reject")
        if (!response.status.isSuccess()) {
            val errorText = response.bodyAsText()
            throw Exception(errorText.ifBlank { "Ошибка: ${response.status.value}" })
        }
    }
}