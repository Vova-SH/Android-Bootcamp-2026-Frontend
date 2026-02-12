package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.sicampus.bootcamp2026.ui.navigation.Root
import ru.sicampus.bootcamp2026.ui.screens.meetings.MeetingsScreen
import ru.sicampus.bootcamp2026.ui.screens.signin.SignIn
import ru.sicampus.bootcamp2026.ui.screens.users.UsersScreen
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //val navController = rememberNavController()
            //val startDestination = TimeTable()
            AndroidBootcamp2026FrontendTheme {
                Root()
            }
        }
    }
}

/*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

AppNavHost(navController, modifier = Modifier.padding(innerPadding))


NavHost(navController = navController, startDestination = SignIn()){
                        composable ("singIn"){ SignIn(Modifier.padding(innerPadding) ) }
                        composable ("sing"){ SignIn(Modifier.padding(innerPadding) ) }
                        composable ("singIn"){ SignIn(Modifier.padding(innerPadding) ) }
                    }


Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    //signUp(Modifier.padding(innerPadding))
                }

            }
        }
    }
}*/
