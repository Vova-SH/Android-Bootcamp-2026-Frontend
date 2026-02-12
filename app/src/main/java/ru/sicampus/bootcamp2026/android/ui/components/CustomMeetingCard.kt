package ru.sicampus.bootcamp2026.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.android.ui.theme.AppTheme
import ru.sicampus.bootcamp2026.android.ui.theme.Black
import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey

data class MeetingUi(
    val organizerName: String,
    val title: String,
    val description: String,
    val dateText: String,
    val timeText: String,
    val isMyMeeting: Boolean
)

sealed interface MeetingCardActions {
    data object None : MeetingCardActions
    data class InviteActions(
        val onAccept: () -> Unit,
        val onDecline: () -> Unit
    ) : MeetingCardActions
}

@Composable
fun MeetingCard(
    meeting: MeetingUi,
    modifier: Modifier = Modifier,
    actions: MeetingCardActions = MeetingCardActions.None
) {
    val cardBg = Color(0xFFE4E4E4)
    val cardShape = RoundedCornerShape(22.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(cardBg)
            .padding(16.dp)
    ) {
        if (meeting.isMyMeeting) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.you_are_organizer),
                fontSize = 14.sp,
                color = Color(0xFF86CD62),
                fontFamily = FontFamily(Font(R.font.open_sans_extrabold))
            )
        }
        else{
            Text(
                text = meeting.organizerName,
                fontSize = 14.sp,
                color = DarkBlue,
                fontFamily = FontFamily(Font(R.font.open_sans_semibold))
            )
        }

        Spacer(Modifier.height(6.dp))

        // Название
        Text(
            text = meeting.title,
            fontSize = 18.sp,
            color = DarkBlue,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.open_sans_extrabold))
        )

        Spacer(Modifier.height(6.dp))

        // Описание
        Text(
            text = meeting.description,
            fontSize = 14.sp,
            color = TextGrey,
            fontFamily = FontFamily(Font(R.font.open_sans_semibold))
        )

        Spacer(Modifier.height(12.dp))

        // Дата
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Дата",
                tint = Black,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = meeting.dateText,
                fontSize = 14.sp,
                color = Black,
                fontFamily = FontFamily(Font(R.font.open_sans_semibold))
            )
        }

        Spacer(Modifier.height(8.dp))

        // Время
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTimeFilled,
                contentDescription = "Время",
                tint = Black,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = meeting.timeText,
                fontSize = 14.sp,
                color = Black,
                fontFamily = FontFamily(Font(R.font.open_sans_semibold))
            )
        }

        // Да/нет встреча
        when (actions) {
            MeetingCardActions.None -> Unit

            is MeetingCardActions.InviteActions -> {
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton(
                        modifier = Modifier.weight(1f),
                        bg = Color(0xFF86CD62),
                        icon = { Icon(Icons.Default.ThumbUp, contentDescription = "Принять", tint = Color.White) },
                        onClick = actions.onAccept
                    )

                    ActionButton(
                        modifier = Modifier.weight(1f),
                        bg = Color(0xFFDF8484),
                        icon = { Icon(Icons.Default.ThumbDown, contentDescription = "Отклонить", tint = Color.White) },
                        onClick = actions.onDecline
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier,
    bg: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            icon()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MeetingCardHomePreview() {
    AppTheme {
        Column(Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MeetingCard(
                meeting = MeetingUi(
                    organizerName = "Кира Йошикаге",
                    title = "Жесткий просмотр тиктоков",
                    description = "Хотим добить огонечек за 300 днееей",
                    dateText = "03.02.2026",
                    timeText = "09:00 - 10:00",
                    isMyMeeting = true
                ),
                actions = MeetingCardActions.None
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeetingCardNotificationsPreview() {
    AppTheme {
        Column(Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MeetingCard(
                meeting = MeetingUi(
                    organizerName = "Зубенко Михаил Петрович",
                    title = "Время работать",
                    description = "Имитация работы в старбаксе у окна",
                    dateText = "04.02.2026",
                    timeText = "14:00 - 15:00",
                    isMyMeeting = false
                ),
                actions = MeetingCardActions.InviteActions(
                    onAccept = {},
                    onDecline = {}
                )
            )
        }
    }
}
