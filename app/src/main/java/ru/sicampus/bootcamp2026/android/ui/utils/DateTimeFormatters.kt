package ru.sicampus.bootcamp2026.android.ui.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val RU = Locale("ru")

private val DATE_FORMAT: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy", RU)

private val TIME_FORMAT: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm", RU)

private fun parseIsoToZdt(iso: String): ZonedDateTime {
    // Поддерживает строки вида "2026-02-04T20:00:14.167Z"
    val instant = Instant.parse(iso)
    return instant.atZone(ZoneId.systemDefault())
}

fun formatDateDdMmYyyy(iso: String): String =
    DATE_FORMAT.format(parseIsoToZdt(iso))

fun formatTimeHm(iso: String): String =
    TIME_FORMAT.format(parseIsoToZdt(iso))

fun formatTimeRange(startIso: String, endIso: String): String =
    "${formatTimeHm(startIso)} – ${formatTimeHm(endIso)}"
