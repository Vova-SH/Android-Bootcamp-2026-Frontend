package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import ru.sicampus.bootcamp2026.ui.theme.BgGradientTop
import ru.sicampus.bootcamp2026.ui.theme.Black

@Composable
fun Title(
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = BgGradientTop)
            .padding(bottom = 20.dp)
            .zIndex(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
            .padding(start = 16.dp)
    ) {
        Text(
            text = title,
            color = Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium
        )
    }
}