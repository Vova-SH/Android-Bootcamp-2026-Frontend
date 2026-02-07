package ru.sicampus.bootcamp2026.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun AppTitle(
    modifier: Modifier = Modifier,
    titleText: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth(),
        text = titleText,
        fontSize = 32.sp,
        fontWeight = FontWeight.W900,
        fontStyle = FontStyle.Normal,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun PreviewAppTitle() {
    AndroidBootcamp2026FrontendTheme {
        AppTitle(
            titleText = "Регистрация"
        )
    }
}