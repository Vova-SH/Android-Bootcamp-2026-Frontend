package ru.sicampus.bootcamp2026.ui.screens.schedule


interface ScheduleState {

    object Loading: ScheduleState
    object DayData: ScheduleState
    object ErrorData: ScheduleState

}