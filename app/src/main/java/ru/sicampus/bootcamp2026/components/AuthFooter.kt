package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.BgGradientBottom
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.PrimaryGray
import kotlin.Boolean

@Composable
fun AuthFooter(
    modifier: Modifier = Modifier,
    primaryButtonText: String,
    primaryButtonOnClick: () -> Unit,
    buttonEnabled: Boolean = true,
    secondaryText1: String,
    secondaryText2: String,
    secondaryText2OnClick: () -> Unit
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(172.dp)
            .background(brush = BgGradientBottom)
            .padding(top = 16.dp)
            .clickable(
                interactionSource = remember() { MutableInteractionSource() },
                indication = null
            ) { }
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppButton(
            text = primaryButtonText,
            onClick = primaryButtonOnClick,
            enabled = buttonEnabled
        )

        Row (
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = secondaryText1,
                fontSize = 16.sp,
                color = PrimaryGray
            )
            Text(
                text = secondaryText2,
                fontSize = 16.sp,
                color = Black,
                modifier = Modifier.clickable { secondaryText2OnClick() }
            )
        }
    }
}
