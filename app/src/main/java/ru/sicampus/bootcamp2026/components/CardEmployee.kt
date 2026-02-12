package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.selectedMeeting
import ru.sicampus.bootcamp2026.ui.theme.Green
import ru.sicampus.bootcamp2026.ui.theme.Grey82
import ru.sicampus.bootcamp2026.ui.theme.Orange
import ru.sicampus.bootcamp2026.ui.theme.Red

@Composable
fun CardEmployee(
    user: UserEntity,
//    invitationState: MeetingEntity.InvitationState,
    onCardClick: () -> Unit = {}
) {
//    val (statusText, statusColor) = when(invitationState) {
//        MeetingEntity.InvitationState.Agree -> "Согласился" to Green
//        MeetingEntity.InvitationState.Disagree -> "Отказаля" to Red
//        MeetingEntity.InvitationState.NoAnswer -> "Ждём ответа" to Orange
//    }
    var isExpandedInCard by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .width(346.dp)
            .height(122.dp)
//            .background(color = GreyForCard, shape = RoundedCornerShape(18.dp)) TODO сменить дизайн, а то на тёмной теме его совсем не видно
            .padding(12.dp)
            .clickable(onClick = onCardClick),
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "фото",
                    modifier = Modifier
                        .padding(end = 8.dp, start = 10.dp)
                        .width(30.dp)
                        .height(30.dp)
                )
                Text(user.surname, fontWeight = FontWeight.W600, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Почта:",
                    color = Grey82,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(end = 6.dp, start = 10.dp)
                )
                Text(
                    text = user.mail,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            IconButton(
                onClick = { isExpandedInCard = true },
                modifier = Modifier.padding(top = 20.dp, start = 70.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Ещё",
                    tint = Color.Black
                )
            }
            DropdownMenu(
                expanded = isExpandedInCard,
                onDismissRequest = { isExpandedInCard = false },
                modifier = Modifier.width(200.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("Изменить права") },
                    onClick = {
                        isExpandedInCard = false
                        //изменение прав
                        val meeting = selectedMeeting ?: return@DropdownMenuItem
//                        if (user in meeting.admins) {
//                            meeting.admins.remove(user)
//                        } else {
//                            meeting.admins.add(user)
//                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Удалить из встречи", color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        isExpandedInCard = false
//                        selectedMeeting?.users?.remove(user)
                    }
                )
            }
//            Text(
//                text = "statusText",
//                color = statusColor,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.W600,
//                modifier = Modifier.padding(top = 80.dp),
//                maxLines = 1
//            )
        }
    }
}

//@Preview
//@Composable
//fun ShowCard() {
//    val user = UserEntity(
//        surname = "Иванов",
//        name = "Иван",
//        patronymic = "Иванович",
//        mail = "ivanov@example.com"
//    )
//    Column {
//        CardEmployee(user, MeetingEntity.InvitationState.Agree)
//        CardEmployee(user, MeetingEntity.InvitationState.Disagree)
//        CardEmployee(user, MeetingEntity.InvitationState.NoAnswer)
//    }
//}