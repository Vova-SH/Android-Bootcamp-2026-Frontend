package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.MeetingDataSource
import ru.sicampus.bootcamp2026.domain.entities.Invitation
import ru.sicampus.bootcamp2026.domain.entities.InvitationStatus
import ru.sicampus.bootcamp2026.domain.entities.Meeting
import ru.sicampus.bootcamp2026.domain.entities.MeetingCreate
import ru.sicampus.bootcamp2026.domain.entities.MeetingMini
import ru.sicampus.bootcamp2026.domain.mapper.InvitationMapper
import ru.sicampus.bootcamp2026.domain.mapper.MeetingMapper
import ru.sicampus.bootcamp2026.utils.SettingsUtils
import java.time.LocalDate

class MeetingRepository(
    private val meetingDataSource: MeetingDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val settingsUtils: SettingsUtils
) {

    suspend fun getDaySchedule(day: LocalDate): Result<List<MeetingMini>> {
        authLocalDataSource.setToken(settingsUtils.getEmail()!!, settingsUtils.getPassword()!!)

        return meetingDataSource.daySchedule(authLocalDataSource.token, day.toString()).map { meetingMiniDtos ->
            meetingMiniDtos.map { MeetingMapper.toDomain(it) }
        }
    }

    suspend fun createMeeting(meetingCreate: MeetingCreate): Result<Meeting> {
        return meetingDataSource.createMeeting(MeetingMapper.toDto(meetingCreate)).map { meetingDto ->
            MeetingMapper.toDomain(meetingDto)
        }
    }
//
//    suspend fun getMeetingById(id: Long): Result<Meeting> {
//        return meetingDataSource.getMeetingById(id).map { meetingDto ->
//            MeetingMapper.toDomain(meetingDto)
//        }
//    }
//
//    suspend fun getWeekSchedule(year: Int, week: Int): Result<Map<LocalDate, List<ru.sicampus.bootcamp2026.domain.entities.MeetingMini>>> {
//        return meetingDataSource.getWeekSchedule(year, week).map { map ->
//            map.mapKeys { LocalDate.parse(it.key) }.mapValues { entry ->
//                entry.value.map { MeetingMapper.toDomain(it) }
//            }
//        }
//    }
//
//    suspend fun getMonthSchedule(year: Int, month: Int): Result<Map<LocalDate, List<ru.sicampus.bootcamp2026.domain.entities.MeetingMini>>> {
//        return meetingDataSource.getMonthSchedule(year, month).map { map ->
//            map.mapKeys { LocalDate.parse(it.key) }
//                .mapValues { entry ->
//                    entry.value.map { MeetingMapper.toDomain(it) }
//            }
//        }
//    }
//
    suspend fun getInvitations(): Result<List<Invitation>> {
        return meetingDataSource.getInvitations().map { invitationDtos ->
            invitationDtos.map { InvitationMapper.toDomain(it) }
        }
    }

    suspend fun respondToInvitation(invitationId: Long, status: InvitationStatus): Result<Unit> {
        return meetingDataSource.respondToInvitation(invitationId, status)
    }
}