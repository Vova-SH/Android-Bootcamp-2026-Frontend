package ru.sicampus.bootcamp2026.ui.screens.incomingbooks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.dto.invitation.UserMiniInvitationDto
import ru.sicampus.bootcamp2026.data.dto.meeting.MeetingDto
import ru.sicampus.bootcamp2026.data.dto.user.UserMiniDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DetailsIncomingMeetingScreen(modifier: Modifier = Modifier) {
//    val meet: MeetingDto =
//        MeetingDto(
//            1, "Ревью дизайна", "312 каб", "Ревью дизайна.",
//            "сб, 28 января",
//            LocalTime.of(11, 0),
//            LocalDateTime.of(2026, 2, 7, 12, 0),
//            organizer = UserMiniDto(1, "Елена", "Босс", "dawdawd"),
//            listOf<UserMiniInvitationDto>(
//                UserMiniInvitationDto(
//                    1L, "Инокентий",
//                    "Лысенко", "Молорик", "Разраб", "1"
//                ),
//                UserMiniInvitationDto(
//                    1L, "Борис",
//                    "Ельцин", "Молорик", "Разраб", "1"
//                ),
//                UserMiniInvitationDto(
//                    1L, "Иван",
//                    "Ургант", "Молорик", "Разраб", "1"
//                )
//            ),
//            createAt = LocalDateTime.of(2026, 2, 7, 12, 0)
//        )
//    Scaffold(
//        topBar = {
//            Box(Modifier
//                .fillMaxWidth()
//                .height(60.dp)
//                .background(Color.White)) {
//                Text(
//                    stringResource(R.string.details), modifier = Modifier.padding(top = 20.dp, start = 20.dp),
//                    fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
//            }
//        },
//        modifier = Modifier,
//        containerColor = Color(0xffEEEEEE)
//    ) { it1 ->
//        LazyColumn(Modifier
//            .padding(top = (it1.calculateTopPadding().value + 20).dp)
//            .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally) {
//            item {
//                Box(modifier = Modifier
//                    .fillMaxWidth(0.95f)
//                    .clip(RoundedCornerShape(15.dp))
//                    .background(Color.White)) {
//                    Column(Modifier.padding(12.dp)){
//                    Text(meet.title, fontWeight = FontWeight.SemiBold,
//                        fontSize = 20.sp)
//                        Spacer(Modifier.size(20.dp))
//                    Row() {
//                        Box(modifier = Modifier
//                            .size(50.dp)
//                            .background(
//                                shape = CircleShape,
//                                color = Color(0xffFFEDD4)
//                            ), contentAlignment = Alignment.Center) {
//                            Icon(
//                                painterResource(R.drawable.schedule), "",
//                                tint = Color(0xffF54900),
//                                modifier = Modifier.fillMaxSize(0.5f)
//                            )
//                        }
//                        Spacer(Modifier.size(10.dp ))
//                        Column() {
//                            Text("Дата и время", fontWeight = FontWeight.Medium,
//                                color = Color(0xff636363).copy(0.7f),
//                                fontSize = 12.sp)
//                            Text("${meet.timeStart.dayOfWeek.getDisplayName(TextStyle.SHORT,
//                                Locale(
//                                "ru")).replaceFirstChar { it.uppercase() }},${meet
//                                    .timeStart.dayOfMonth} ${meet
//                                        .timeStart.month
//                                        .getDisplayName(TextStyle.FULL, Locale("ru"))}",
//                                fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
//                            Spacer(Modifier.size(1.dp))
//                            Text("${meet.timeStart.format(DateTimeFormatter.ofPattern(
//                                "HH:mm", Locale("ru")))}-${meet.timeEnd.format(DateTimeFormatter.ofPattern(
//                                "HH:mm", Locale("ru")))}")
//                        }
//                    }
//                        Spacer(Modifier.size(12.dp))
//                        HorizontalDivider()
//                        Spacer(Modifier.size(12.dp))
//                        Row() {
//                            Box(
//                                modifier = Modifier
//                                    .size(50.dp)
//                                    .background(
//                                        shape = CircleShape,
//                                        color = Color(0xffF3E8FF)
//                                    ), contentAlignment = Alignment.Center
//                            ) {
//                                Icon(
//                                    painterResource(R.drawable.location), "",
//                                    tint = Color(0xffA32BFB),
//                                    modifier = Modifier.fillMaxSize(0.5f)
//                                )
//                            }
//                            Column(Modifier.padding(start = 10.dp, top = 6.dp)) {
//                                Text("Место проведения", color = Color(0xff636363).copy(0.7f),
//                                    fontSize = 12.sp, fontWeight = FontWeight.Medium)
//                                Text(text = meet.address, fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
//                                    modifier = Modifier.padding(top = 5.dp))
//                            }
//                        }
//                        Spacer(Modifier.size(12.dp))
//                        HorizontalDivider()
//                        Spacer(Modifier.size(12.dp))
//                        Row() {
//                            Box(
//                                modifier = Modifier
//                                    .size(50.dp)
//                                    .background(
//                                        shape = CircleShape,
//                                        color = Color(0xffDBEAFE)
//                                    ), contentAlignment = Alignment.Center
//                            ) {
//                                Icon(
//                                    painterResource(R.drawable.location), "",
//                                    tint = Color(0xff155DFC),
//                                    modifier = Modifier.fillMaxSize(0.5f)
//                                )
//                            }
//                            Column(Modifier.padding(start = 10.dp, top = 6.dp)) {
//                                Text("Место проведения", color = Color(0xff636363).copy(0.7f),
//                                    fontSize = 12.sp, fontWeight = FontWeight.Medium)
//                                Text(text = "${meet.organizer.firstName} ${meet.organizer.secondName}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
//                                    modifier = Modifier.padding(top = 5.dp))
//                            }
//                        }
//                        Spacer(Modifier.size(12.dp))
//                        HorizontalDivider()
//                        Spacer(Modifier.size(12.dp))
//                        Text("Описание", fontSize = 12.sp, color = Color(0xff636363).copy(0.7f))
//                        Spacer(Modifier.size(10.dp))
//                        Text(meet.description.toString())
//                        Spacer(Modifier.size(12.dp))
//                    }
//                }
//            }
//            item {
//                Box(modifier = Modifier
//                    .padding(top = 16.dp)
//                    .fillMaxWidth(0.95f)
//                    .clip(RoundedCornerShape(15.dp))
//                    .background(Color.White)) {
//                    Column(Modifier.padding(12.dp)){
//                        Text("Участники", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
//                        Spacer(Modifier.size(20.dp))
//                        meet.users.forEach { us ->
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Box(modifier = Modifier
//                                    .size(35.dp)
//                                    .background(
//                                        shape = CircleShape,
//                                        color = Color(0xffFFEDD4)
//                                    ), contentAlignment = Alignment.Center) {
//                                    Icon(
//                                        painterResource(R.drawable.profile), "",
//                                        tint = Color(0xffF54900),
//                                        modifier = Modifier.fillMaxSize(0.5f)
//                                    )
//                                }
//                                Spacer(Modifier.size(10.dp))
//                                Text("${us.firstName} ${us.secondName}",
//                                    fontWeight = FontWeight.SemiBold,
//                                    fontSize = 14.sp)
//                            }
//                            Spacer(Modifier.size(5.dp))
//                        }
//                    }
//                }
//            }
//            item {
//                Spacer(Modifier.size(10.dp))
//                Row(horizontalArrangement = Arrangement.SpaceBetween,
//                    modifier = Modifier.fillMaxWidth(0.95f)) {
//                    Button(onClick = {}, modifier = Modifier.weight(1f),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff155DFC),
//                            contentColor = Color.White)) {
//                        Text("Принять")
//                    }
//                    Spacer(Modifier.size(20.dp))
//                    Button(onClick = {}, modifier = Modifier.weight(1f),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.White,
//                            contentColor = Color.Black)) {
//                        Text("Отклонить")
//                    }
//                }
//            }
//        }
//    }
}


@Preview
@Composable
private fun dwdawdawd() {
    DetailsIncomingMeetingScreen()
}