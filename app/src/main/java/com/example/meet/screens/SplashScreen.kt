package com.example.meet.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meet.R
import com.example.meet.ui.theme.MeetTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit={},
    modifier: Modifier = Modifier
) {
    val (progress, setProgress) = remember { mutableStateOf(0f) }
    var isFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        val durationMs = 16000L

        while (!isFinished) {
            val elapsed = System.currentTimeMillis() - startTime
            val newProgress = if (elapsed >= durationMs) {
                isFinished = true
                1f
            } else {
                elapsed.toFloat() / durationMs
            }

            setProgress(newProgress) // ✅ правильно
            delay(16)
        }

        delay(500)
        onSplashFinished()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_meet_planner),
                contentDescription = "Логотип приложения",
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = "Планировщик\nвстреч",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center
            )

            AnimatedVisibility(
                visible = !isFinished,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    progress = { progress }, // ← передаём текущее значение
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    MeetTheme {
        SplashScreen()
    }
}