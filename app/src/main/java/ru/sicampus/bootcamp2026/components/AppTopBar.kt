package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import ru.sicampus.bootcamp2026.ui.theme.BgGradientTop
import ru.sicampus.bootcamp2026.ui.theme.Black

@Composable
fun AppTopBar(
    title: String,
    startIconId: Int,
    startIconButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier,
    endIconId: Int? = null,
    endIconButtonOnClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = BgGradientTop)
            .padding(bottom = 24.dp)
            .zIndex(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                iconId = startIconId,
                onClick = startIconButtonOnClick
            )
            endIconId?.let {
                IconButton(
                    iconId = endIconId,
                    onClick = endIconButtonOnClick ?: {}
                )
            }
        }
        Text(
            text = title,
            fontSize = 24.sp,
            color = Black,
            fontWeight = FontWeight.Medium,
        )
    }
}