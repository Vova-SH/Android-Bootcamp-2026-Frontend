package ru.sicampus.bootcamp2026.android.ui.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val RU = Locale("ru")

private val DATE_FORMAT =
    DateTimeFormatter.ofPattern("dd.MM.yyyy", RU)

private val TIME_FORMAT =
    DateTimeFormatter.ofPattern("HH:mm", RU)

private fun parseIsoToLocal(iso: String): LocalDateTime {
    return Instant
        .parse(iso)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun formatDateDdMmYyyy(iso: String): String =
    DATE_FORMAT.format(parseIsoToLocal(iso))

fun formatTimeHm(iso: String): String =
    TIME_FORMAT.format(parseIsoToLocal(iso))

fun formatTimeRange(startIso: String, endIso: String): String =
    "${formatTimeHm(startIso)} – ${formatTimeHm(endIso)}"
