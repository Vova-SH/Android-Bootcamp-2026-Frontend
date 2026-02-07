package ru.sicampus.bootcamp2026.ui.utils

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatUtcToLocal(utcString: String?, pattern: String): String {
    if (utcString.isNullOrBlank()) return ""
    return try {
        val odt = OffsetDateTime.parse(utcString)
        val zdt = odt.atZoneSameInstant(ZoneId.systemDefault())
        zdt.format(DateTimeFormatter.ofPattern(pattern, Locale("ru")))
    } catch (e: Exception) {
        utcString
    }
}