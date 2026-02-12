package ru.sicampus.bootcamp2026.ui.screens.schedule

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.MeetingDataSource
import ru.sicampus.bootcamp2026.domain.usecase.ViewScheduleUseCase
import ru.sicampus.bootcamp2026.domain.usecase.meeting.GetDayScheduleUseCase
import ru.sicampus.bootcamp2026.utils.SettingsUtils

object ScheduleViewModelFactory {
    fun create(context: Context): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val authLocalDataSource = AuthLocalDataSource
            val settingsUtils = SettingsUtils(context)
            val meetingDataSource = MeetingDataSource()
            val meetingRepository = MeetingRepository(meetingDataSource, authLocalDataSource, settingsUtils)
            val getDayScheduleUseCase = GetDayScheduleUseCase(meetingRepository)

            ScheduleViewModel(getDayScheduleUseCase)
        }
    }
}