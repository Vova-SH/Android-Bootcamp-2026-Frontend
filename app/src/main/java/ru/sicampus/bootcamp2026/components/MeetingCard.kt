package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.Green
import ru.sicampus.bootcamp2026.ui.theme.LiteGray
import ru.sicampus.bootcamp2026.ui.theme.Red
import ru.sicampus.bootcamp2026.ui.theme.White

data class MeetingUi(
    val mode: String,
    val place: String,
    val title: String,
    val time: String,
    val host: String
)

@Composable
fun MeetingCard(
    meeting: MeetingUi,
    modifier: Modifier = Modifier
) {
    val statusColor = when (meeting.mode) {
        "Оффлайн" -> Blue
        "Гибрид"  -> Green
        "Онлайн"  -> Red
        else      -> LiteGray
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Gray)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // цветная точка статуса
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = meeting.mode,
                    style = MaterialTheme.typography.bodySmall,
                    color = White
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = meeting.place,
                    style = MaterialTheme.typography.bodySmall,
                    color = White
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = meeting.title,
                style = MaterialTheme.typography.titleMedium,
                color = White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = meeting.time,
                style = MaterialTheme.typography.bodyMedium,
                color = White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = meeting.host,
                style = MaterialTheme.typography.bodyMedium,
                color = White
            )
        }
    }
}