package ru.sicampus.bootcamp2026.data

import kotlinx.coroutines.delay
import ru.sicampus.bootcamp2026.data.model.MeetingDto

class MeetingRepository {
    suspend fun getMeetings(): List<MeetingDto> {
        delay(1000)
        return listOf(
            MeetingDto(
                id = 1,
                title = "Дейли",
                startTime = "10:00",
                endTime = "11:00",
                description = "",
                colorHex = "#F3E8FF"
            ),
            MeetingDto(
                id = 2,
                title = "Обсуждение API",
                startTime = "14:00",
                endTime = "15:00",
                description = "Для нового сервиса",
                colorHex = "#E0F2FE"
            ),
            MeetingDto(
                id = 3,
                title = "Код ревью",
                startTime = "16:00",
                endTime = "17:00",
                description = "Делим монолит",
                colorHex = "#DCFCE7"
            ),
            MeetingDto(
                id = 4,
                title = "Test",
                startTime = "21:00",
                endTime = "22:00",
                description = "VeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongTest",
                colorHex = "#BCFCE7"
            )
        )
    }
}