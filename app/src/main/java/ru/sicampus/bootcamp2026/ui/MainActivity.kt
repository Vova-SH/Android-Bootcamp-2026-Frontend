package ru.sicampus.bootcamp2026.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import ru.sicampus.bootcamp2026.ui.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = androidx.compose.ui.graphics.Color.White) {
                    HomeScreen(
                        onNavigateToCreate = {},
                        onNavigateToDetails = {},
                        onNavigateToProfile = {},
                        currentTab = 1,
                        onTabSelected = {}
                    )
                }
            }
        }
    }
}