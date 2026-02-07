package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.PrimaryGray
import ru.sicampus.bootcamp2026.ui.theme.SecondaryGray
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    activeColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Black,
        contentColor = White
    ),
    disabledColors: ButtonColors = ButtonDefaults.buttonColors(
        disabledContainerColor = PrimaryGray,
        disabledContentColor = SecondaryGray
    )
) {
    Button (
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(20.dp),
        enabled = enabled,
        colors = if (enabled) activeColors else disabledColors,
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
