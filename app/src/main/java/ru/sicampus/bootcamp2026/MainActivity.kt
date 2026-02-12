package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.authorization.presentation.AuthScreenViewModel
import com.example.create_meet.presentation.AddMeetingViewModel
import com.example.create_meet.presentation.EventsListScreenViewModel
import com.example.navigation.AppNavigation
import com.example.navigation.NavigationViewModel
import com.example.registration.presentation.RegisterScreenViewModel
import com.example.user_main.presentation.UserMainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthScreenViewModel = hiltViewModel()
            val registerViewModel: RegisterScreenViewModel = hiltViewModel()
            val navigationViewModel: NavigationViewModel = hiltViewModel()
            val userScreenViewModel: UserMainScreenViewModel = hiltViewModel()
            val eventsListScreenViewModel: EventsListScreenViewModel = hiltViewModel()

            AndroidBootcamp2026FrontendTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        AppNavigation(
                            authViewModel = authViewModel,
                            registerScreenViewModel = registerViewModel,
                            navigationViewModel = navigationViewModel,
                            userMainScreenViewModel = userScreenViewModel,
                            eventsListScreenViewModel = eventsListScreenViewModel,
                        )
                    }

                }
            }
        }
    }
}