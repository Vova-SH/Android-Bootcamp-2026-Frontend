package ru.sicampus.bootcamp2026.ui.screens.book

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.sicampus.bootcamp2026.data.repository.MeetingRepository
import ru.sicampus.bootcamp2026.data.repository.UserRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.MeetingDataSource
import ru.sicampus.bootcamp2026.data.source.UserDataSource
import ru.sicampus.bootcamp2026.domain.usecase.meeting.CreateMeetingUseCase
import ru.sicampus.bootcamp2026.domain.usecase.user.SearchUserUseCase
import ru.sicampus.bootcamp2026.utils.SettingsUtils

object BookViewModelFactory {
    fun create(context: Context): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val settingsUtils = SettingsUtils(context)
            val authLocalDataSource = AuthLocalDataSource

            val meetingDataSource = MeetingDataSource()
            val meetingRepository = MeetingRepository(meetingDataSource, authLocalDataSource, settingsUtils)
            val createMeetingUseCase = CreateMeetingUseCase(meetingRepository)

            val userDataSource = UserDataSource()
            val userRepository = UserRepository(userDataSource, authLocalDataSource, settingsUtils)
            val searchUserUseCase = SearchUserUseCase(userRepository)

            BookViewModel(createMeetingUseCase, searchUserUseCase, settingsUtils)
        }
    }
}