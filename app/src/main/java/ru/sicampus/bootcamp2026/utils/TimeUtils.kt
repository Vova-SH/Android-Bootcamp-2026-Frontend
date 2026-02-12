package ru.sicampus.bootcamp2026.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter



object TimeUtils {

    fun timeHourMinutes(time: LocalTime?): String? {
        if (time == null) return null
        val formatter24 = DateTimeFormatter.ofPattern("HH:mm")
        return time.format(formatter24)
    }

}