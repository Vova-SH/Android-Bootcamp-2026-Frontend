package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.Network
import ru.sicampus.bootcamp2026.data.dto.InvitationDTO

class InboxDataSource {
    suspend fun getInvitations(): Result<List<InvitationDTO>> = withContext(Dispatchers.IO){
        runCatching {
            val result = Network.client.get("api/invitation/active")
            if (result.status != HttpStatusCode.OK){
                error("Status: ${result.status}")
            }
            result.body()
        }
    }
}