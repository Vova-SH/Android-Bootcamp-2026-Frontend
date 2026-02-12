package ru.sicampus.bootcamp2026

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ru.sicampus.bootcamp2026.components.MainTab
import ru.sicampus.bootcamp2026.screens.CalendarScreen
import ru.sicampus.bootcamp2026.screens.EventCreateScreen
import ru.sicampus.bootcamp2026.screens.HomeScreen
import ru.sicampus.bootcamp2026.screens.InvitationsScreen
import ru.sicampus.bootcamp2026.screens.AuthMode
import ru.sicampus.bootcamp2026.screens.AuthScreen
import ru.sicampus.bootcamp2026.screens.ProfileScreen
import ru.sicampus.bootcamp2026.screens.SettingsScreen
import ru.sicampus.bootcamp2026.screens.WelcomeScreen
import ru.sicampus.bootcamp2026.data.remote.AuthStore

private sealed interface RootScreen {
    data object Welcome : RootScreen
    data class Auth(val mode: AuthMode) : RootScreen
    data object Main : RootScreen
    data object Invitations : RootScreen
}

@Composable
fun AppRoot() {
    var currentScreen by remember { mutableStateOf<RootScreen>(RootScreen.Welcome) }
    var currentTab by remember { mutableStateOf(MainTab.Home) }

    val authedUser by AuthStore.user.collectAsState()

    LaunchedEffect(authedUser) {
        // Если появился пользователь -> пускаем в приложение.
        if (authedUser != null) {
            currentScreen = RootScreen.Main
        } else {
            // Если разлогинились -> обратно на Welcome.
            if (currentScreen != RootScreen.Welcome) {
                currentTab = MainTab.Home
                currentScreen = RootScreen.Welcome
            }
        }
    }

    fun goMain(tab: MainTab? = null) {
        if (tab != null) currentTab = tab
        currentScreen = RootScreen.Main
    }

    when (currentScreen) {
        RootScreen.Welcome -> WelcomeScreen(
            onRegisterClick = { currentScreen = RootScreen.Auth(AuthMode.Register) },
            onLoginClick = { currentScreen = RootScreen.Auth(AuthMode.Login) }
        )

        is RootScreen.Auth -> {
            val auth = currentScreen as RootScreen.Auth
            AuthScreen(
            startMode = auth.mode,
            onAuthSuccess = { goMain(MainTab.Home) },
            onBack = { currentScreen = RootScreen.Welcome }
        )
        }

        RootScreen.Main -> {
            when (currentTab) {
                MainTab.Home -> HomeScreen(
                    currentTab = currentTab,
                    onTabSelected = { tab -> currentTab = tab }
                )

                MainTab.Calendar -> CalendarScreen(
                    currentTab = currentTab,
                    onTabSelected = { tab -> currentTab = tab }
                )

                MainTab.Add -> EventCreateScreen(
                    currentTab = currentTab,
                    onTabSelected = { tab -> currentTab = tab }
                )

                MainTab.Profile -> ProfileScreen(
                    currentTab = currentTab,
                    onTabSelected = { tab -> currentTab = tab },
                    onOpenInvitations = {
                        // Экран "Приглашения" не является вкладкой в Bottom Bar,
                        // поэтому открываем его как отдельный root-screen.
                        currentScreen = RootScreen.Invitations
                    },
                    onOpenSettings = {
                        // Настройки — отдельная вкладка.
                        goMain(MainTab.Settings)
                    },
                    onLogout = {
                        // Выход: очищаем сессию.
                        AuthStore.clear()
                        currentTab = MainTab.Home
                        currentScreen = RootScreen.Welcome
                    }
                )

                MainTab.Settings -> SettingsScreen(
                    currentTab = currentTab,
                    onTabSelected = { tab -> currentTab = tab }
                )
            }
        }

        RootScreen.Invitations -> InvitationsScreen(
            currentTab = MainTab.Profile,
            onTabSelected = { tab ->
                // При переключении вкладки возвращаемся в Main.
                currentTab = tab
                currentScreen = RootScreen.Main
            }
        )
    }
}
