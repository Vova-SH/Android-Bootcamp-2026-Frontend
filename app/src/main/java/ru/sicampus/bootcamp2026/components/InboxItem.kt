package ru.sicampus.bootcamp2026.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.domain.entities.InvitationEntity
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.PrimaryGray
import ru.sicampus.bootcamp2026.ui.theme.Red
import ru.sicampus.bootcamp2026.ui.theme.SecondaryGray
import ru.sicampus.bootcamp2026.ui.theme.White
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.text.format

@Composable
fun InboxItem(
    invitation: InvitationEntity,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SecondaryGray)
            .border(
                width = 1.dp,
                color = PrimaryGray,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp, 24.dp, 20.dp, 20.dp)
    ) {
        Status(
            invitation
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = invitation.meeting.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Black,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = invitation.meeting.description,
            fontSize = 14.sp,
            color = Black,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateAndTime(
            invitation
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppButton(
                text = "Принять",
                onClick = {
                    onAccept()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
            AppButton(
                text = "Отклонить",
                onClick = {
                    onDecline()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                activeColors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = Black
                ),
            )
        }
    }
}

@Composable
private fun Status(
    invitation: InvitationEntity
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(8.dp)
                .background(Red)
        )
        Text(
            text = invitation.message,
            fontSize = 14.sp,
            color = Red
        )
    }
}

@Composable
private fun DateAndTime(
    invitation: InvitationEntity
) {
    val isoDateTime = invitation.meeting.startTime
    val dateTime = try {
        Log.d("InboxItem", "Parsing ISO date time: $isoDateTime")
        val instant = Instant.parse("${isoDateTime}+03:00")
        val zonedDateTime = instant.atZone(ZoneId.of("UTC+03:00"))
        val date = zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
        val time = zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        Log.d("InboxItem", "Parsed date: $date, time: $time")
        Pair(date, time)
    } catch (e: Exception) {
        Log.e("InboxItem", "Error parsing date: ${e.message}", e)
        Pair("", "")
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_date),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = Black
            )
            Text(
                text = dateTime.first,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Black,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_time),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = Black
            )
            Text(
                text = dateTime.second,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Black,
            )
        }
    }
}
