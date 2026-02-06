package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.model.ParticipantStatus
import ru.sicampus.bootcamp2026.domain.util.Result
import java.util.UUID

/**
 * Репозиторий для работы с приглашениями
 */
interface InvitationRepository {

    /**
     * Получение списка приглашений
     */
    suspend fun getInvitations(): Result<List<Invitation>>

    /**
     * Получение деталей приглашения
     */
    suspend fun getInvitationDetails(meetingId: UUID): Result<Invitation>

    /**
     * Ответ на приглашение
     */
    suspend fun respondToInvitation(
        meetingId: UUID,
        status: ParticipantStatus
    ): Result<Invitation>
}

