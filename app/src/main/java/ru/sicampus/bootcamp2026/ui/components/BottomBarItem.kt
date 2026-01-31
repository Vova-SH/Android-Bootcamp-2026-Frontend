package ru.sicampus.bootcamp2026.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val LightGreen = Color(0xFFBBDBA6)

@Composable
fun BottomBarItem (
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
){

    //Анимация изменения цвета фона для элементов NavigationBottomBar (прозрачный <-> светло-зеленый)
    val backgroundColor by animateColorAsState(
        targetValue = if(isSelected) LightGreen  else Color.Transparent,
        label ="bgColorAnimation"
    )

    //Анимация изменения цвета иконки для элементов NavigationBottomBar (черный <-> белый)
    val iconColor by animateColorAsState(
        targetValue = if(isSelected) Color.Black else Color.White,
        label = "iconColorAnimation"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(percent = 40))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor
        )
    }
}

