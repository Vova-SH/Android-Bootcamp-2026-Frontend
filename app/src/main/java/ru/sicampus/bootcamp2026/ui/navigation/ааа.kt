/*
package ru.sicampus.bootcamp2026.ui.navigation

package ru.sicampus.bootcamp2026.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.ui.screens.Notifications
import ru.sicampus.bootcamp2026.ui.screens.profile.Profile
import ru.sicampus.bootcamp2026.ui.screens.signin.SignIn
import ru.sicampus.bootcamp2026.ui.screens.signin.SignInState
import ru.sicampus.bootcamp2026.ui.screens.SignUp
import ru.sicampus.bootcamp2026.ui.screens.timetable.AddInvite
import ru.sicampus.bootcamp2026.ui.screens.timetable.TimeTable

*/
/*объявление интерфейса для "объединения" путей для навигации и ограничения количества наследников*//*

sealed interface Route
@Serializable
object SignIn: Route
@Serializable
object SignUp: Route
@Serializable
object TimeTable: Route
@Serializable
object Profile: Route
@Serializable
object Notifications: Route
@Serializable
object AddInvite: Route

*/
/*@Serializable
object goToApp*//*



@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Root () {
    val navController = rememberNavController()
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    */
/** из документациии андройд по панеле навигации: https://developer.android.com/develop/ui/compose/components/navigation-bar?hl=ru
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) } **//*

    var authState by remember {
        mutableStateOf(SignInState.Data(userLoggedIn = true, isEnabledSend = false, error = null))
        // todo: userLoggedIn = false   !!!!!!!!   !!!!!!!  !!!!!  !!!!!!  !!! !!!!!!!
    }
    val showAddButton = selectedItem == 0 && authState.userLoggedIn == true && не AppInvite
            Scaffold(
                modifier = Modifier,
                topBar = {
                    if (authState.userLoggedIn == true) {
                        LargeTopAppBar(
                            title = {
                                Text(
                                    when (selectedItem) {
                                        0 -> "Ваше расписание"
                                        1 -> "Профиль"
                                        2 -> "Приглашения"
                                        else -> ""
                                    }
                                )
                            }
                        )
                    }
                },
                bottomBar = {
                    if (authState.userLoggedIn == true) {
                        NavigationBar() {
                            NavigationBarItem(
                                selected = selectedItem == 0,
                                onClick = {
                                    selectedItem = 0
                                    navController.navigate(TimeTable)
                                },
                                icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                                label = { Text("Расписание") }
                            )
                            NavigationBarItem(
                                selected = selectedItem == 1,
                                onClick = {
                                    selectedItem = 1
                                    navController.navigate(Profile)
                                },
                                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                                label = { Text("Профиль") }
                            )
                            NavigationBarItem(
                                selected = selectedItem == 2,
                                onClick = {
                                    selectedItem = 2
                                    navController.navigate(Notifications)
                                },
                                icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                                label = { Text("Приглашения") }
                            )
                        }
                    }
                },
                floatingActionButton = {
                    if (showAddButton) {
                        FloatingActionButton(onClick = {
                            navController.navigate(AddInvite)
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.End,
                */
/*NavHost(navController = navController, startDestination = SignIn) {
                    composable<SignIn> {
                        SignIn(
                            modifier = Modifier.padding(innerPadding),
                            onNavigateToSignUp = {
                                navController.navigate(route = SignUp)
                            }
                        )
                    }
                    composable<SignUp> {
                        SignUp()
                    }
                    //navigation<goToApp>(startDestination = TimeTable) {}
                }*//*

            ){ innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = if (authState.userLoggedIn == true) TimeTable else SignIn,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<SignIn> {
                        SignIn(
                            navController = navController
                        )
                    }
                    composable<SignUp> {
                        SignUp(
                            onNavigateToTimeTable = {
                                // временная логика :)
                                authState = authState.copy(userLoggedIn = true)
                                navController.navigate(TimeTable) {
                                    popUpTo(SignIn) { inclusive = true } // нельзя вернуться
                                }
                            }
                        )
                    }
                    composable<TimeTable> {

                        TimeTable()
                    }
                    composable<Profile> {
                        Profile()
                    }
                    composable<Notifications> {
                        Notifications()
                    }
                    composable<AddInvite> {
                        AddInvite()
                    }
                }
            }
}
и как мне сделать что бы компка ухадила при открытом экране AddInvite*/
