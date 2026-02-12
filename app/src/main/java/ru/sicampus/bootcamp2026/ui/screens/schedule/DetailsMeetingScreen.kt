package ru.sicampus.bootcamp2026.ui.screens.schedule

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DetailsMeetingScreen(modifier: Modifier = Modifier, context: Context,
                         index: Int = 0, vm: ScheduleViewModel
) {
    val meet = vm.state.collectAsState().value.dayMeetings[index]
    Scaffold(
        topBar = {
            Box(Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White)) {
                Text(
                    stringResource(R.string.details), modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                    fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }
        },
        modifier = Modifier,
        containerColor = Color(0xffEEEEEE)
    ) { it1 ->
        LazyColumn(Modifier
            .padding(top = (it1.calculateTopPadding().value + 20).dp)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Box(modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)) {
                    Column(Modifier.padding(12.dp)){
                    Text(meet.title, fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp)
                        Spacer(Modifier.size(20.dp))
                    Row() {
                        Box(modifier = Modifier
                            .size(50.dp)
                            .background(
                                shape = CircleShape,
                                color = Color(0xffFFEDD4)
                            ), contentAlignment = Alignment.Center) {
                            Icon(
                                painterResource(R.drawable.schedule), "",
                                tint = Color(0xffF54900),
                                modifier = Modifier.fillMaxSize(0.5f)
                            )
                        }
                        Spacer(Modifier.size(10.dp ))
                        Column() {
                            Text("Дата и время", fontWeight = FontWeight.Medium,
                                color = Color(0xff636363).copy(0.7f),
                                fontSize = 12.sp)
                            Text("${meet.date.format(DateTimeFormatter.ofPattern(
                                "MMM dd", Locale("ru")
                            ))}",
                                fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Spacer(Modifier.size(1.dp))
                            Text("${meet.timeStart.format(DateTimeFormatter.ofPattern(
                                "HH:mm", Locale("ru")))}-${meet.timeEnd.format(DateTimeFormatter.ofPattern(
                                "HH:mm", Locale("ru")))}")
                        }
                    }
                        Spacer(Modifier.size(12.dp))
                        HorizontalDivider()
                        Spacer(Modifier.size(12.dp))
                        Row() {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        shape = CircleShape,
                                        color = Color(0xffF3E8FF)
                                    ), contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painterResource(R.drawable.location), "",
                                    tint = Color(0xffA32BFB),
                                    modifier = Modifier.fillMaxSize(0.5f)
                                )
                            }
                            Column(Modifier.padding(start = 10.dp, top = 6.dp)) {
                                Text("Место проведения", color = Color(0xff636363).copy(0.7f),
                                    fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text(text = meet.address, fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 5.dp))
                            }
                        }
                        Spacer(Modifier.size(12.dp))
                        HorizontalDivider()
                        Spacer(Modifier.size(12.dp))
                        Row() {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        shape = CircleShape,
                                        color = Color(0xffDBEAFE)
                                    ), contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painterResource(R.drawable.org_icon), "",
                                    tint = Color(0xff155DFC),
                                    modifier = Modifier.fillMaxSize(0.5f)
                                )
                            }
                            Column(Modifier.padding(start = 10.dp, top = 6.dp)) {
                                Text("Организатор", color = Color(0xff636363).copy(0.7f),
                                    fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text(text = "${meet.organizer.firstName} ${meet.organizer.secondName}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 5.dp))
                            }
                        }
                        Spacer(Modifier.size(12.dp))
                        HorizontalDivider()
                        Spacer(Modifier.size(12.dp))
                        Text("Описание", fontSize = 12.sp, color = Color(0xff636363).copy(0.7f))
                        Spacer(Modifier.size(10.dp))
                        Text(meet.description?: "Без описания.")
                        Spacer(Modifier.size(12.dp))
                    }
                }
            }

        }
    }
}


//@Preview
//@Composable
//private fun dwdawdawd() {
//    DetailsMeetingScreen()
//}