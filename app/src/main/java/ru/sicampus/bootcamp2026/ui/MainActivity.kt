package ru.sicampus.bootcamp2026.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import ru.sicampus.bootcamp2026.ui.main.MainScreen // Импортируем наш новый экран-контейнер

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                enableEdgeToEdge()
                //Связываем MainScreen с навигацией
                MainScreen(
                    onNavigateToCreate = {
                        // Тут будет навигация на экран создания
                    },
                    onNavigateToDetails = { meetingId ->
                        // Тут будет навигация на детали (id: $meetingId)
                    },
                    onNavigateToProfile = {
                        // Тут будет навигация в профиль
                    }
                )
            }
        }
    }
}