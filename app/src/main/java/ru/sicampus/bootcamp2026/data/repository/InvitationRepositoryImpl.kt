package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.mapper.toDomain
import ru.sicampus.bootcamp2026.data.remote.api.InvitationApi
import ru.sicampus.bootcamp2026.data.remote.dto.InvitationActionRequest
import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.model.PaginatedData
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

    override suspend fun getInvitations(page: Int, size: Int): Result<PaginatedData<Invitation>> {
        return try {
            val response = invitationApi.getInvitations()
            val invitations = response.map { it.toDomain() }

            // API не поддерживает пагинацию, эмулируем её локально
            val start = page * size
            val end = minOf(start + size, invitations.size)
            val paginatedContent = if (start < invitations.size) {
                invitations.subList(start, end)
            } else {
                emptyList()
            }

            Result.Success(
                PaginatedData(
                    content = paginatedContent,
                    totalPages = (invitations.size + size - 1) / size,
                    totalElements = invitations.size.toLong(),
                    number = page,
                    size = size,
                    first = page == 0,
                    last = end >= invitations.size,
                    empty = invitations.isEmpty()
                )
            )
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
        invitationId: UUID,
        accept: Boolean
    ): Result<Invitation> {
        return try {
            val status = if (accept) "CONFIRMED" else "DECLINED"
            val response = invitationApi.respondToInvitation(
                meetingId = invitationId,
                request = InvitationActionRequest(status = status)
            )
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

