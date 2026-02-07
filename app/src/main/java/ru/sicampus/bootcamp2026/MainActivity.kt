package ru.sicampus.bootcamp2026

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.screens.Login.LoginScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.ui.theme.screens.Invitation.InvitationListScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.CreateMeetingScreen

import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.MeetingInfoScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.MeetingInfoState
import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.MeetingInfoViewModel
import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.MeetingResponseScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.MeetingResponseState
import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.MeetingResponseViewModel
import ru.sicampus.bootcamp2026.ui.theme.screens.Profile.ProfileScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.TimeTable.TimetableScreen

class MainActivity() : ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme{
                val viewModel: AppViewModel = viewModel()
                val meetingResponseVm: MeetingResponseViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return MeetingResponseViewModel(viewModel) as T
                        }
                    }
                )
                val meetingState by meetingResponseVm.uiState.collectAsState()

                val state by viewModel.appState.collectAsState()
                val context = LocalContext.current
                val userPreferences = remember { UserPreferences(context) }
                when(val currState = state) {
                    is ViewModelState.Login -> LoginScreen(viewModel, userPreferences)
                    is ViewModelState.Invitations-> InvitationListScreen(viewModel)
                    is ViewModelState.TimeTable -> TimetableScreen(viewModel, userPreferences)
                    is ViewModelState.Profile -> ProfileScreen(viewModel, userPreferences)
                    is ViewModelState.MeetingResponse -> {
                        when (val state = meetingState) {
                            is MeetingResponseState.Content ->
                                MeetingResponseScreen(
                                    users = state.users,
                                    appViewModel = viewModel
                                )
                            is MeetingResponseState.Loading ->
                                CircularProgressIndicator()
                            is MeetingResponseState.Error ->
                                Text("Ошибка: ${state.reason}")
                        }
                    }
                    ViewModelState.CreateMeeting -> CreateMeetingScreen(
                        userPreferences = userPreferences,
                        appViewModel = viewModel
                    )
                    //is ViewModelState.Loading ->
                    //is ViewModelState.Error ->
                    else -> LoginScreen(viewModel, userPreferences)
                }
            }
        }

    }
}



@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    AndroidBootcamp2026FrontendTheme {
    }
}