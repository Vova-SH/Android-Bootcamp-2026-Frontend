package ru.sicampus.bootcamp2026.android.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R




@Composable
fun CustomButton (
    text: String,
    containerColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean = true
){
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
            color = textColor,
        )
    }
}