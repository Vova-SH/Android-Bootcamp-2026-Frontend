package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.InvitationCreateDto
import ru.sicampus.bootcamp2026.data.source.InvitationService
import ru.sicampus.bootcamp2026.domain.model.Invitation
import ru.sicampus.bootcamp2026.domain.model.InvitationStatus
import ru.sicampus.bootcamp2026.domain.repository.InvitationRepository

class InvitationRepositoryImpl : InvitationRepository {
    private val service = InvitationService()

    override suspend fun getInvitations(): Result<List<Invitation>> {
        return try {
            val dtos = service.getAllInvitations()
            val domain = dtos.map { dto ->
                Invitation(
                    id = dto.id,
                    meetingId = dto.meetingId,
                    status = mapStatus(dto.status),
                    createdAt = dto.createdAt
                )
            }
            Result.success(domain)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendInvitation(email: String, meetingId: Long): Result<Unit> {
        return try {
            service.sendInvitation(InvitationCreateDto(email, meetingId))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptInvitation(id: Long): Result<Unit> {
        return try {
            service.acceptInvitation(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun rejectInvitation(id: Long): Result<Unit> {
        return try {
            service.rejectInvitation(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapStatus(status: String): InvitationStatus {
        return when (status) {
            "ACCEPTED" -> InvitationStatus.ACCEPTED
            "REJECTED" -> InvitationStatus.REJECTED
            "AWAITS" -> InvitationStatus.AWAITS
            else -> InvitationStatus.UNKNOWN
        }
    }
}