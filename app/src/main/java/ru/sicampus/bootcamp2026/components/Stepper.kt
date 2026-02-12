package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StepperComponent(
    items: List<String>,
    currentIndex: Int,
    onIndexChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) {
        Text("Нет элементов")
        return
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Кнопка назад
        Button(
            onClick = {
                if (currentIndex > 0) {
                    onIndexChanged(currentIndex - 1)
                }
            },
            enabled = currentIndex > 0
        ) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = items[currentIndex],
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Кнопка вперед
        Button(
            onClick = {
                if (currentIndex < items.size - 1) {
                    onIndexChanged(currentIndex + 1)
                }
            },
            enabled = currentIndex < items.size - 1
        ) {
            Text("Вперёд")
        }
    }
}