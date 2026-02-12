package com.example.planify.main.common.utils

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

fun LocalDate.pageForDate(
    initialPage: Int
): Int = ChronoUnit.DAYS.between(LocalDate.now(), this)
    .toInt() + initialPage

fun Int.dateForPage(initialPage: Int): LocalDate =
    LocalDate.now().plusDays((this - initialPage).toLong())

fun Int.monthForPage(initialPage: Int): YearMonth =
    YearMonth.now().plusMonths((this - initialPage).toLong())