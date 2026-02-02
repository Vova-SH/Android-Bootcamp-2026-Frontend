package ru.sicampus.bootcamp2026.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun MeetIcon() {
    Card(modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .size(64.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blue,
        ),
        ) {
        Icon(painter = painterResource(R.drawable.meeting)
            , contentDescription = "icon", tint = White,
            modifier = Modifier.fillMaxSize().padding(5.dp)
        )
    }
}