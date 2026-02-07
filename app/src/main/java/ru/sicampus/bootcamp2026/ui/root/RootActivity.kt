package ru.sicampus.bootcamp2026.ui.root

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.ui.login.LoginActivity
import ru.sicampus.bootcamp2026.utils.SettingsUtils

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (!SettingsUtils(App.context).checkProfileExists()) {
                this.startActivity(
                    Intent(this, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                )
            }

            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ){
                AppNavHost(navController, it,this)
            }
        }
    }
}