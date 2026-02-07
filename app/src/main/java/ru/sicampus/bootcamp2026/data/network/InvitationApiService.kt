package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import ru.sicampus.bootcamp2026.data.dto.InvitationDto
import ru.sicampus.bootcamp2026.data.dto.PageDto

class InvitationApiService {
    private val client = Network.client

    suspend fun getInvitations(page: Int = 0, size: Int = 20): List<InvitationDto> {
        val response: PageDto<InvitationDto> = client.get("${Network.HOST}/api/invitations") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }.body()
        return response.content
    }

    suspend fun getMyInvitations(page: Int = 0, size: Int = 20): List<InvitationDto> {
        val response: PageDto<InvitationDto> = client.get("${Network.HOST}/api/invitations/my") {
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
            }
        }.body()
        return response.content
    }

    suspend fun createInvitation(invitation: InvitationDto): InvitationDto {
        return client.post("${Network.HOST}/api/invitations") {
            setBody(invitation)
        }.body()
    }

    suspend fun updateInvitation(id: Long, invitation: InvitationDto): InvitationDto {
        return client.put("${Network.HOST}/api/invitations/$id") {
            setBody(invitation)
        }.body()
    }
}
