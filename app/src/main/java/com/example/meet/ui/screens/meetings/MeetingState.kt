package com.example.meet.ui.screens.meetings


import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalTime

@Stable
data class MeetingState(
    val title: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.of(9, 0),
    val isRecurring: Boolean = false,
    val participants: List<Participant> = emptyList(),
    val location: String = ""
)

data class Participant(
    val id: String,
    val name: String,
    val avatarColor: Color = Color(0xFF0088FF)
)