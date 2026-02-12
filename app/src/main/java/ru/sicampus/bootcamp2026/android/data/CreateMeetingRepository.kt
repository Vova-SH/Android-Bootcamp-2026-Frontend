package ru.sicampus.bootcamp2026.android.data

import ru.sicampus.bootcamp2026.android.data.dto.InvitationDTO
import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.data.source.InvitationNetworkDataSource
import ru.sicampus.bootcamp2026.android.data.source.MeetingsNetworkDataSource
import ru.sicampus.bootcamp2026.android.data.source.PersonsNetworkDataSource
import ru.sicampus.bootcamp2026.android.data.source.PersonResponse

class CreateMeetingRepository(
    private val meetingsNetworkDataSource: MeetingsNetworkDataSource,
    private val invitationNetworkDataSource: InvitationNetworkDataSource,
    private val personNetworkDataSource: PersonsNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun searchPersonByEmail(email: String): Result<PersonResponse> {
        return personNetworkDataSource.getPersonByEmail(email)
    }

    suspend fun createMeetingWithInvitations(
        title: String,
        description: String,
        startAt: String,
        endAt: String,
        inviteeIds: List<Long>
    ): Result<MeetingResponse> {
        val organizerId = authLocalDataSource.getUserId()
            ?: return Result.failure(Exception("User ID not found"))

        return meetingsNetworkDataSource.createMeeting(
            organizerId = organizerId,
            title = title,
            description = description,
            startAt = startAt,
            endAt = endAt
        ).mapCatching { meetingResponse ->
            inviteeIds.forEach { inviteeId ->
                invitationNetworkDataSource.createInvitation(
                    meetingId = meetingResponse.id,
                    inviteeId = inviteeId
                ).getOrThrow()
            }
            meetingResponse
        }
    }

    suspend fun searchPersonsByPartialEmail(query: String): Result<List<PersonResponse>> {
        return personNetworkDataSource.getAllPersons()
            .map { pageResponse ->
                pageResponse.content.filter { person ->
                    person.email.contains(query, ignoreCase = true)
                }
            }
    }
}