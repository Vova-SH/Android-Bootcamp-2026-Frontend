package ru.sicampus.bootcamp2026.data.util

import ru.sicampus.bootcamp2026.components.MeetingUi
import ru.sicampus.bootcamp2026.data.dto.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.MeetingTypeDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.Instant
import java.time.ZoneId

fun MeetingDto.toMeetingUi(fallbackDate: LocalDate = LocalDate.now()): Pair<LocalDate, MeetingUi> {
    val start = startAt?.let { parseIsoLocalDateTime(it) }
    val end = endAt?.let { parseIsoLocalDateTime(it) }

    val date = start?.toLocalDate() ?: fallbackDate

    val st = start?.toLocalTime()?.toString()?.take(5)
    val en = end?.toLocalTime()?.toString()?.take(5)
    val timeLabel = when {
        st != null && en != null -> "$st – $en"
        st != null -> st
        else -> ""
    }

    val modeLabel = when (type) {
        MeetingTypeDto.ONLINE -> "Онлайн"
        MeetingTypeDto.OFFLINE -> "Оффлайн"
        MeetingTypeDto.HYBRID -> "Гибрид"
        else -> ""
    }

    val placeLabel = when {
        !location.isNullOrBlank() -> location!!
        !url.isNullOrBlank() -> "Онлайн"
        else -> ""
    }

    val ui = MeetingUi(
        mode = modeLabel.ifBlank { "Оффлайн" },
        place = placeLabel,
        title = title ?: "(Без названия)",
        time = timeLabel,
        host = "Организатор"
    )

    return date to ui
}

private fun parseIsoLocalDateTime(raw: String): LocalDateTime? {
    val t = raw.trim()
    if (t.isBlank()) return null
    return runCatching { LocalDateTime.parse(t) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(t).toLocalDateTime() }.getOrNull()
        ?: runCatching { Instant.parse(t).atZone(ZoneId.systemDefault()).toLocalDateTime() }.getOrNull()
}
