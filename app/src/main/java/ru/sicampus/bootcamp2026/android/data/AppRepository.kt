package ru.sicampus.bootcamp2026.android.data

import ru.sicampus.bootcamp2026.android.data.dto.InvitationDTO
import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import ru.sicampus.bootcamp2026.android.data.mapper.toEntity
import ru.sicampus.bootcamp2026.android.data.source.ApiDataSource
import ru.sicampus.bootcamp2026.android.domain.entities.UserEntity

class AppRepository(
    private val api: ApiDataSource
) {
    suspend fun getUsers(): Result<List<UserEntity>> =
        api.getUsers().map { list -> list.map { it.toEntity() } }

    suspend fun getMeetings(): Result<List<MeetingResponse>> =
        api.getMeetings()

    suspend fun getInvitations(): Result<List<InvitationDTO>> =
        api.getInvitations()
}