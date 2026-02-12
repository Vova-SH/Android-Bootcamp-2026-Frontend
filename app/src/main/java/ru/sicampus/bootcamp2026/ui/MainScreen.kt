package ru.sicampus.bootcamp2026.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.nav.AppRoute
import ru.sicampus.bootcamp2026.ui.nav.bottom.BottomNavigation
import ru.sicampus.bootcamp2026.ui.nav.bottom.NavGraph
import ru.sicampus.bootcamp2026.ui.screen.modal.AddMeetingScreen
import ru.sicampus.bootcamp2026.ui.screen.modal.ModalScreen
import ru.sicampus.bootcamp2026.ui.screen.modal.rememberModalScreenController
import ru.sicampus.bootcamp2026.ui.screen.profile.ProfileIntent
import ru.sicampus.bootcamp2026.ui.screen.profile.ProfileViewModel

@Preview
@Composable
fun MainScreen() {
    val bottomNavController = rememberNavController()
    val currentBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val destination = currentBackStackEntry?.destination

    val activity = LocalActivity.current as ComponentActivity
    val profileVm = viewModel<ProfileViewModel>(activity)
    val isEditMode by profileVm.isEditMode.collectAsState()

    val modalController = rememberModalScreenController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavGraph(bottomNavController)
        Box(
            modifier = Modifier
                .padding(bottom = 128.dp, end = 16.dp)
                .align(Alignment.BottomEnd)
                .zIndex(1f)
        ) {
            when {
                destination?.hasRoute<AppRoute.ProfileRoute>() == true -> {
                    if (!isEditMode) {
                        FloatingActionButton(
                            iconId = R.drawable.ic_edit,
                            onClick = {
                                val currentState = profileVm.uiState.value
                                if (currentState is ru.sicampus.bootcamp2026.ui.screen.profile.ProfileState.Content) {
                                    profileVm.prepareEditMode(currentState.user)
                                } else {
                                    profileVm.onIntent(ProfileIntent.SetEditMode)
                                }
                            }
                        )
                    }
                }

                destination?.hasRoute<AppRoute.InboxRoute>() == true || destination?.hasRoute<AppRoute.MeetingsRoute>() == true -> {
                    FloatingActionButton(
                        iconId = R.drawable.ic_add,
                        onClick = {
                            modalController.show()
                        }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .zIndex(1f)
        ) {
            BottomNavigation(bottomNavController)
        }

        ModalScreen(
            controller = modalController
        ) { hide ->
            AddMeetingScreen(onClose = hide)
        }
    }
}