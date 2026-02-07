package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun RefText(fontSize: TextUnit = 16.sp, text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = fontSize,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.clickable(
            onClick = onClick
        ),
        textDecoration = TextDecoration.Underline,
    )
}

@Composable
fun SimpleHeader(
    text: String
) {
    Text(
        text = text,
        fontSize = 25.sp
    )
}