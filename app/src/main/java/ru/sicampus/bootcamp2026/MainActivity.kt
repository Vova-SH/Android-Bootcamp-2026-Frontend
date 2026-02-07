package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ru.sicampus.bootcamp2026.android.ui.nav.NavigationGraph
import ru.sicampus.bootcamp2026.android.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NavigationGraph(
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }
    }
}