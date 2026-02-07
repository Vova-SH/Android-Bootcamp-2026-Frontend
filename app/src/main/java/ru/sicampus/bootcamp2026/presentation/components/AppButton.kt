package ru.sicampus.bootcamp2026.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

sealed class ButtonContent {
    data class Text(
        val text: String,
        val style: TextStyle? = null
    ) : ButtonContent()

    data class Icon(
        val icon: Painter,
        val size: Dp
    ) : ButtonContent()
}

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    content: ButtonContent,
    cornerRadius: Dp = 0.dp,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = if (cornerRadius > 0.dp) {
            RoundedCornerShape(cornerRadius)
        } else {
            RectangleShape
        },
        contentPadding = contentPadding,
        onClick = onClick,
        content = {
            when (content) {
                is ButtonContent.Text -> {
                    Text(
                        text = content.text,
                        style = content.style ?: MaterialTheme.typography.labelMedium
                    )
                }

                is ButtonContent.Icon -> {
                    Icon(
                        modifier = Modifier.size(content.size),
                        painter = content.icon,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewAppButton() {
    AndroidBootcamp2026FrontendTheme {
        AppButton(
            content = ButtonContent.Icon(
                icon = painterResource(R.drawable.ic_arrow_back),
                size = 16.dp
            ),
            cornerRadius = 4.dp,
            onClick = {}
        )
    }
}