package ru.sicampus.bootcamp2026.data.util


fun compactTime(raw: String?): String? {
    val t = raw?.trim().orEmpty()
    if (t.isBlank()) return null
    return if (t.length >= 5) t.take(5) else t
}
