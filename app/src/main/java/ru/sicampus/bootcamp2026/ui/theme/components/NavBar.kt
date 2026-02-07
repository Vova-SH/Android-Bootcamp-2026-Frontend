package ru.sicampus.bootcamp2026.ui.theme.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.screens.Invitation.InvitationListContent
import ru.sicampus.bootcamp2026.ui.theme.screens.Invitation.InvitationListScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.Profile.ProfileNoEdContent
import ru.sicampus.bootcamp2026.ui.theme.screens.Profile.ProfileScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.TimeTable.TimeTableContent
import ru.sicampus.bootcamp2026.ui.theme.screens.TimeTable.TimetableScreen


@Composable
fun BottomNavBar(selectedItem: String, onItemSelected: (String) -> Unit, appViewModel: AppViewModel) {
    NavigationBar {
        val items = listOf("Приглашения", "Расписание", "Профиль")

        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = {
                    onItemSelected(item)
                    when (item) {
                        "Приглашения" -> appViewModel.NavigateTo(ViewModelState.Invitations)
                        "Расписание" -> appViewModel.NavigateTo(ViewModelState.TimeTable)
                        "Профиль" -> appViewModel.NavigateTo(ViewModelState.Profile)
                    }
                },
                icon = {
                    when (item) {
                        "Приглашения" -> Icon(
                            Icons.Default.MailOutline,
                            contentDescription = "Приглашения"
                        )
                        "Расписание" -> Icon(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = "Расписание"
                        )
                        "Профиль" -> Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Профиль"
                        )
                    }
                },
                label = { Text(item) }
            )
        }
    }
}



