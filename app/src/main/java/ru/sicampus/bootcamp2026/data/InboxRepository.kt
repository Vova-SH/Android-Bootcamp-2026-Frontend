package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.dto.toEntity
import ru.sicampus.bootcamp2026.data.source.InboxDataSource
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity

class InboxRepository(
    private val inboxDataSource: InboxDataSource
) {
    suspend fun getActiveInvitations() : Result<List<InvitationEntity>>{
        return inboxDataSource.getInvitations().mapCatching { dtoList ->
            dtoList.map { it.toEntity() }
        }
    }
}