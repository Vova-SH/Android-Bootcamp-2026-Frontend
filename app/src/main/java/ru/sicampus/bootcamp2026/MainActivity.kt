package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import ru.sicampus.bootcamp2026.ui.screens.AuthScreen
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // NavigationHost тута надо но мне чота впадлу пока что
                    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
                        AuthScreen(onLoginClick = {}, onRegisterClick = {})
                    }
                }
            }
        }
    }
}