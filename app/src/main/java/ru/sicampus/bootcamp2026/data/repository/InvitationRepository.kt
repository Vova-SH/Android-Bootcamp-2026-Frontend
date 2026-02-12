package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.AnswerInvitationRequestDTO
import ru.sicampus.bootcamp2026.data.dto.toEntity
import ru.sicampus.bootcamp2026.data.source.InvitationDataSource
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity

class InvitationRepository(
    private val invitationDataSource: InvitationDataSource
) {
    suspend fun getActiveInvitations() : Result<List<InvitationEntity>>{
        return invitationDataSource.getInvitations().mapCatching { dtoList ->
            dtoList.map { it.toEntity() }
        }
    }
    suspend fun answerInvitation(id : Int, isAccepted : Boolean) : Result<Unit>{
        return invitationDataSource.answerInvitation(
            AnswerInvitationRequestDTO(
                id = id,
                status = if (isAccepted) "ACCEPTED" else "DECLINED"
            )
        )
    }
}