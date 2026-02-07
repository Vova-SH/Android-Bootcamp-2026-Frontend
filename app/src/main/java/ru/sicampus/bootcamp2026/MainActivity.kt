package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.ui.app.MainApp
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthLocalDataSource.init(this)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme {
                MainApp()
            }
        }
    }
}
