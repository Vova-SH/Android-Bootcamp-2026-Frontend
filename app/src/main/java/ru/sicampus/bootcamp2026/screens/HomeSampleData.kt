package ru.sicampus.bootcamp2026.screens

import ru.sicampus.bootcamp2026.components.EventType
import ru.sicampus.bootcamp2026.components.MeetingUi
import java.time.LocalDate

fun modeToEventType(mode: String): EventType = when (mode) {
    "Оффлайн" -> EventType.Offline
    "Гибрид"  -> EventType.Hybrid
    "Онлайн"  -> EventType.Online
    else      -> EventType.Another
}

fun sampleHomeMeetings(today: LocalDate): List<DatedMeeting> =
    listOf(
        DatedMeeting(
            date = today,
            meeting = MeetingUi(
                mode = "Оффлайн",
                place = "Кабинет 138, C2",
                title = "Итоги рабочей недели",
                time = "15:00 – 16:00",
                host = "Аркадий Юрьевич Волож"
            )
        ),
        DatedMeeting(
            date = today,
            meeting = MeetingUi(
                mode = "Онлайн",
                place = "Zoom",
                title = "Созвон по проекту",
                time = "18:00 – 19:00",
                host = "Куратор курса"
            )
        ),

        DatedMeeting(
            date = today.plusDays(1),
            meeting = MeetingUi(
                mode = "Гибрид",
                place = "Аудитория 204",
                title = "Разбор домашек",
                time = "11:00 – 12:00",
                host = "Преподаватель"
            )
        ),

        DatedMeeting(
            date = today.plusDays(2),
            meeting = MeetingUi(
                mode = "Онлайн",
                place = "Teams",
                title = "Статус по спринту",
                time = "10:00 – 10:30",
                host = "Тимлид"
            )
        ),

        DatedMeeting(
            date = today.withDayOfMonth(5),
            meeting = MeetingUi(
                mode = "Онлайн",
                place = "Zoom",
                title = "Онбординг стажёров",
                time = "10:00 – 11:00",
                host = "HR отдел"
            )
        ),
        DatedMeeting(
            date = today.withDayOfMonth(5),
            meeting = MeetingUi(
                mode = "Оффлайн",
                place = "Переговорка 3",
                title = "Планирование спринта",
                time = "12:00 – 13:00",
                host = "Тимлид команды"
            )
        ),


        DatedMeeting(
            date = today.withDayOfMonth(12),
            meeting = MeetingUi(
                mode = "Онлайн",
                place = "Teams",
                title = "Дейли стендап",
                time = "09:30 – 09:45",
                host = "Тимлид"
            )
        ),
        DatedMeeting(
            date = today.withDayOfMonth(12),
            meeting = MeetingUi(
                mode = "Оффлайн",
                place = "Кабинет 220",
                title = "Консультация по проекту",
                time = "11:00 – 12:00",
                host = "Научный руководитель"
            )
        ),
        DatedMeeting(
            date = today.withDayOfMonth(12),
            meeting = MeetingUi(
                mode = "Гибрид",
                place = "Лекторий / Zoom",
                title = "Митап по UX",
                time = "14:00 – 15:30",
                host = "Дизайн-команда"
            )
        ),
        DatedMeeting(
            date = today.withDayOfMonth(12),
            meeting = MeetingUi(
                mode = "Онлайн",
                place = "Zoom",
                title = "Созвон с заказчиком",
                time = "17:00 – 18:00",
                host = "Аккаунт-менеджер"
            )
        )
    )
