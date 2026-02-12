package ru.sicampus.bootcamp2026.data.repo

import ru.sicampus.bootcamp2026.data.dto.InvitationDto
import ru.sicampus.bootcamp2026.data.dto.InvitationStatusDto
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.dto.UserRegisterDto
import ru.sicampus.bootcamp2026.data.remote.ApiClient
import ru.sicampus.bootcamp2026.data.remote.BootcampApi
import ru.sicampus.bootcamp2026.data.remote.AuthStore

class BootcampRepository(
    private val api: BootcampApi = ApiClient.api
) {

    // Users
    suspend fun register(dto: UserRegisterDto): UserDto = api.register(dto)

    suspend fun getAllUsers(): List<UserDto> = api.getAllUsers()

    suspend fun login(login: String, password: String): UserDto {
        val creds = AuthStore.Credentials(login = login, password = password)
        AuthStore.setCredentials(creds)
        return try {
            val user = api.login()
            AuthStore.setSession(creds, user)
            user
        } catch (e: Exception) {
            AuthStore.clear()
            throw e
        }
    }

    suspend fun updateMe(dto: UserDto): UserDto {
        val updated = api.updateMe(dto)
        AuthStore.setUser(updated)
        return updated
    }

    suspend fun getMeetingById(id: Long): MeetingDto = api.getMeetingById(id)
    suspend fun createMeeting(dto: MeetingDto): MeetingDto = api.createMeeting(dto)
    suspend fun updateMeeting(id: Long, dto: MeetingDto): MeetingDto = api.updateMeeting(id, dto)

    suspend fun getDaySchedule(dateIso: String): List<MeetingDto> = api.getDaySchedule(dateIso)

    suspend fun getWeekSchedule(): List<MeetingDto> = api.getWeekSchedule()
    suspend fun getTwoWeeksSchedule(): List<MeetingDto> = api.getTwoWeeksSchedule()
    suspend fun getMonthSchedule(): List<MeetingDto> = api.getMonthSchedule()

    suspend fun getMyInvitations(): List<InvitationDto> = api.getMyInvitations()

    suspend fun respondToInvitation(invitationId: Long, status: InvitationStatusDto) {
        val resp = api.respondToInvitation(invitationId, status)
        if (!resp.isSuccessful) {
            throw IllegalStateException("Ошибка ответа на приглашение: HTTP ${resp.code()}")
        }
    }

    fun logout() {
        AuthStore.clear()
    }
}
