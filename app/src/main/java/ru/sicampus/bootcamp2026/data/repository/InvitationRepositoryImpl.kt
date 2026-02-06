package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.mapper.toDomain
import ru.sicampus.bootcamp2026.data.remote.api.InvitationApi
import ru.sicampus.bootcamp2026.data.remote.dto.InvitationActionRequest
import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.model.ParticipantStatus
import ru.sicampus.bootcamp2026.domain.repository.InvitationRepository
import ru.sicampus.bootcamp2026.domain.util.Result
import java.util.UUID
import javax.inject.Inject

/**
 * Реализация репозитория для работы с приглашениями
 */
class InvitationRepositoryImpl @Inject constructor(
    private val invitationApi: InvitationApi
) : InvitationRepository {

    override suspend fun getInvitations(): Result<List<Invitation>> {
        return try {
            val response = invitationApi.getInvitations()
            Result.Success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getInvitationDetails(meetingId: UUID): Result<Invitation> {
        return try {
            val response = invitationApi.getInvitationDetails(meetingId)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun respondToInvitation(
        meetingId: UUID,
        status: ParticipantStatus
    ): Result<Invitation> {
        return try {
            val response = invitationApi.respondToInvitation(
                meetingId = meetingId,
                request = InvitationActionRequest(status = status.name)
            )
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

