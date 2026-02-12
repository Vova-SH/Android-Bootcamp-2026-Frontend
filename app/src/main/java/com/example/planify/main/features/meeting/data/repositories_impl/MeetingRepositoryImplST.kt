package com.example.planify.main.features.meeting.data.repositories_impl

import com.example.planify.main.features.meeting.domain.repositories.MeetingRepository
import com.example.planify.main.features.meeting.entities.Invite
import com.example.planify.main.features.meeting.entities.Meeting
import com.example.planify.main.features.meeting.entities.MeetingInfo
import com.example.planify.main.features.meeting.entities.MeetingInviteStatus
import com.example.planify.main.features.profile.entities.Profile
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

object HardcodedWeekData {

    // Предопределенные пользователи (без userId)
    private val alex = Profile(
        firstName = "Александр",
        lastName = "Иванов",
        position = "Team Lead",
        department = "Разработка",
        profileImageUrl = "https://example.com/avatars/alex.jpg"
    )

    private val maria = Profile(
        firstName = "Мария",
        lastName = "Петрова",
        position = "Frontend Developer",
        department = "Разработка",
        profileImageUrl = "https://example.com/avatars/maria.jpg"
    )

    private val ivan = Profile(
        firstName = "Иван",
        lastName = "Сидоров",
        position = "Backend Developer",
        department = "Разработка",
        profileImageUrl = "https://example.com/avatars/ivan.jpg"
    )

    private val anna = Profile(
        firstName = "Анна",
        lastName = "Смирнова",
        position = "Product Manager",
        department = "Менеджмент",
        profileImageUrl = "https://example.com/avatars/anna.jpg"
    )

    private val dmitry = Profile(
        firstName = "Дмитрий",
        lastName = "Кузнецов",
        position = "UI/UX Designer",
        department = "Дизайн",
        profileImageUrl = "https://example.com/avatars/dmitry.jpg"
    )

    private val sofia = Profile(
        firstName = "София",
        lastName = "Попова",
        position = "QA Engineer",
        department = "Тестирование",
        profileImageUrl = "https://example.com/avatars/sofia.jpg"
    )

    // Маппинг профилей к их ID (для ownerId и senderId)
    private val profileToId = mapOf(
        alex to 101L,
        maria to 102L,
        ivan to 103L,
        anna to 104L,
        dmitry to 105L,
        sofia to 106L
    )

    // Получить ID профиля
    private fun getProfileId(profile: Profile): Long {
        return profileToId[profile] ?: 0L
    }

    // Получить профиль по ID
    private fun getProfileById(id: Long): Profile? {
        return profileToId.entries.find { it.value == id }?.key
    }

    // ID встреч
    private val meetingIds = listOf(1001L, 1002L, 1003L, 1004L, 1005L, 1006L)

    // Генератор инвайтов
    private fun createInvite(
        meetingId: Long,
        sender: Profile,
        status: MeetingInviteStatus,
        createdAt: LocalDateTime,
        updatedAtOffsetHours: Long = 0
    ): Invite {
        return Invite(
            uuid = UUID.randomUUID().toString(),
            meetingId = meetingId,
            senderId = getProfileId(sender),
            status = status,
            createdAt = createdAt,
            updatedAt = createdAt.plusHours(updatedAtOffsetHours)
        )
    }

    // Простое расписание на неделю (1-2 встречи в день)
    fun getWeekMeetings(): HashMap<LocalDate, List<MeetingInfo>> {
        val meetingsMap = HashMap<LocalDate, List<MeetingInfo>>()

        val today = LocalDate.now()
        val now = LocalDateTime.now()
        val monday = today.with(java.time.DayOfWeek.MONDAY)

        // ПОНЕДЕЛЬНИК - 1 встреча
        meetingsMap[monday] = listOf(
            MeetingInfo(
                meeting = Meeting(
                    id = meetingIds[0],
                    ownerId = getProfileId(anna),  // Анна - владелец
                    title = "Планирование недели",
                    description = "Обсуждение задач и целей на неделю",
                    timeStart = LocalDateTime.of(monday, LocalTime.of(10, 0)),
                    duration = Duration.ofMinutes(60),
                    location = "Конференц-зал A"
                ),
                invites = listOf(
                    createInvite(
                        meetingIds[0],
                        anna,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(2)
                    ),
                    createInvite(
                        meetingIds[0],
                        anna,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(2)
                    ),
                    createInvite(meetingIds[0], anna, MeetingInviteStatus.PENDING, now.minusDays(2))
                ),
                participants = listOf(anna, alex, maria)
            )
        )

        // ВТОРНИК - 1 встреча
        val tuesday = monday.plusDays(1)
        meetingsMap[tuesday] = listOf(
            MeetingInfo(
                meeting = Meeting(
                    id = meetingIds[1],
                    ownerId = getProfileId(alex),  // Александр - владелец
                    title = "Технический стендап",
                    description = "Ежедневное обсуждение технических вопросов",
                    timeStart = LocalDateTime.of(tuesday, LocalTime.of(9, 30)),
                    duration = Duration.ofMinutes(30),
                    location = "Zoom"
                ),
                invites = listOf(
                    createInvite(
                        meetingIds[1],
                        alex,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(1)
                    ),
                    createInvite(
                        meetingIds[1],
                        alex,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(1)
                    ),
                    createInvite(
                        meetingIds[1],
                        alex,
                        MeetingInviteStatus.REJECTED,
                        now.minusDays(1),
                        2
                    )
                ),
                participants = listOf(alex, ivan, sofia)
            )
        )

        // СРЕДА - 2 встречи
        val wednesday = monday.plusDays(2)
        meetingsMap[wednesday] = listOf(
            MeetingInfo(
                meeting = Meeting(
                    id = meetingIds[2],
                    ownerId = getProfileId(dmitry),  // Дмитрий - владелец
                    title = "Дизайн-ревью",
                    description = "Обсуждение новых макетов интерфейса",
                    timeStart = LocalDateTime.of(wednesday, LocalTime.of(11, 0)),
                    duration = Duration.ofMinutes(45),
                    location = "Переговорная B"
                ),
                invites = listOf(
                    createInvite(
                        meetingIds[2],
                        dmitry,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(3)
                    ),
                    createInvite(
                        meetingIds[2],
                        dmitry,
                        MeetingInviteStatus.PENDING,
                        now.minusDays(3)
                    )
                ),
                participants = listOf(dmitry, anna)
            ),
            MeetingInfo(
                meeting = Meeting(
                    id = meetingIds[3],
                    ownerId = getProfileId(anna),  // Анна - владелец
                    title = "Демонстрация продукта",
                    description = "Показ новых функций заказчику",
                    timeStart = LocalDateTime.of(wednesday, LocalTime.of(14, 30)),
                    duration = Duration.ofMinutes(90),
                    location = "Главный зал"
                ),
                invites = listOf(
                    createInvite(
                        meetingIds[3],
                        anna,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(3)
                    ),
                    createInvite(
                        meetingIds[3],
                        anna,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(3)
                    ),
                    createInvite(
                        meetingIds[3],
                        anna,
                        MeetingInviteStatus.RESCHEDULE_REQUESTED,
                        now.minusDays(3),
                        5
                    )
                ),
                participants = listOf(anna, alex, dmitry)
            )
        )

        // ЧЕТВЕРГ - 1 встреча
        val thursday = monday.plusDays(3)
        meetingsMap[thursday] = listOf(
            MeetingInfo(
                meeting = Meeting(
                    id = meetingIds[4],
                    ownerId = getProfileId(maria),  // Мария - владелец
                    title = "Код-ревью",
                    description = "Разбор новой функциональности фронтенда",
                    timeStart = LocalDateTime.of(thursday, LocalTime.of(16, 0)),
                    duration = Duration.ofMinutes(60),
                    location = "Google Meet"
                ),
                invites = listOf(
                    createInvite(
                        meetingIds[4],
                        maria,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(4)
                    ),
                    createInvite(
                        meetingIds[4],
                        maria,
                        MeetingInviteStatus.PENDING,
                        now.minusDays(4)
                    )
                ),
                participants = listOf(maria, alex, ivan)
            )
        )

        // ПЯТНИЦА - 1 встреча
        val friday = monday.plusDays(4)
        meetingsMap[friday] = listOf(
            MeetingInfo(
                meeting = Meeting(
                    id = meetingIds[5],
                    ownerId = getProfileId(alex),  // Александр - владелец
                    title = "Ретроспектива недели",
                    description = "Подведение итогов недели и планирование следующей",
                    timeStart = LocalDateTime.of(friday, LocalTime.of(17, 0)),
                    duration = Duration.ofMinutes(60),
                    location = "Конференц-зал A"
                ),
                invites = listOf(
                    createInvite(
                        meetingIds[5],
                        alex,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(5)
                    ),
                    createInvite(
                        meetingIds[5],
                        alex,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(5)
                    ),
                    createInvite(
                        meetingIds[5],
                        alex,
                        MeetingInviteStatus.ACCEPTED,
                        now.minusDays(5)
                    )
                ),
                participants = listOf(alex, maria, ivan, anna, dmitry, sofia)
            )
        )

        return meetingsMap
    }
}

object MeetingRepositoryImplST: MeetingRepository {
    override suspend fun fetchMeetingsInfo(): Map<LocalDate, List<MeetingInfo>> {
        delay(2000)
        return HardcodedWeekData.getWeekMeetings()
    }
}