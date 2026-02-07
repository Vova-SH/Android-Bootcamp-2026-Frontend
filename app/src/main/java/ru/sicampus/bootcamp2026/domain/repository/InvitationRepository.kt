package ru.sicampus.bootcamp2026.domain.repository

import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.model.PaginatedData
import ru.sicampus.bootcamp2026.domain.util.Result
import java.util.UUID

/**
 * Репозиторий для работы с приглашениями
 */
interface InvitationRepository {

    /**
     * Получение списка приглашений
     */
    suspend fun getInvitations(page: Int = 0, size: Int = 20): Result<PaginatedData<Invitation>>

    /**
     * Получение деталей приглашения
     */
    suspend fun getInvitationDetails(meetingId: UUID): Result<Invitation>

    /**
     * Ответ на приглашение
     */
    suspend fun respondToInvitation(
        invitationId: UUID,
        accept: Boolean
    ): Result<Invitation>
}

