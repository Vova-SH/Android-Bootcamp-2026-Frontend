package ru.sicampus.bootcamp2026.screens

import android.R
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.components.MeetingField
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.White
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen() {
    val scrollState1 = rememberScrollState()
    val scrollState2 = rememberScrollState()


    Column(modifier = Modifier.fillMaxSize().background(White)) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .height(389.dp),
            colors = CardDefaults.cardColors(contentColor = White),
            shape = RoundedCornerShape(bottomStart = 30.dp,
                bottomEnd = 30.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        )
        {
            Text(text = "Jan, 2026",
                fontSize = 30.sp,
                color = Black,
                fontFamily = FontFamily(androidx.
                compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_bold)),
                modifier = Modifier.padding(horizontal = 24.dp)
                    .padding(top=60.dp)
            )
            Row (modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top=47.dp)
                .horizontalScroll(scrollState1)){
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Blue)
                    .height(83.dp)
                    .width(74.dp)
                    .clip(RoundedCornerShape(30.dp))) {
                    Text(text = "28",
                        fontSize = 20.sp,
                        color = White,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_bold)),
                        modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.CenterHorizontally) )
                    Text(text = "Ср",
                        fontSize = 20.sp,
                        color = White,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_regular)),
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterHorizontally) )

                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Transparent)
                    .height(83.dp)
                    .width(74.dp)
                    .clip(RoundedCornerShape(30.dp))) {
                    Text(text = "29",
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_bold)),
                        modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.CenterHorizontally) )
                    Text(text = "Чт",
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_regular)),
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterHorizontally) )

                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Transparent)
                    .height(83.dp)
                    .width(74.dp)
                    .clip(RoundedCornerShape(30.dp))) {
                    Text(text = "30",
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_bold)),
                        modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.CenterHorizontally) )
                    Text(text = "Пт",
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_regular)),
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterHorizontally) )

                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Transparent)
                    .height(83.dp)
                    .width(74.dp)
                    .clip(RoundedCornerShape(30.dp))) {
                    Text(text = "31",
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_bold)),
                        modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.CenterHorizontally) )
                    Text(text = "Сб",
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_regular)),
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterHorizontally) )

                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Transparent)
                    .height(83.dp)
                    .width(74.dp)
                    .clip(RoundedCornerShape(30.dp))) {
                    Text(text = "1",
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_bold)),
                        modifier = Modifier
                            .padding(5.dp)
                            .align(Alignment.CenterHorizontally) )
                    Text(text = "Вс",
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_regular)),
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterHorizontally) )

                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }

        Text(text = "Встречи",
            fontSize = 20.sp,
            color = Black,
            fontFamily = FontFamily(androidx.
            compose.ui.text.font.Font(ru.sicampus.bootcamp2026.R.font.montserrat_bold)),
            modifier = Modifier.padding(horizontal = 24.dp)
                .padding(top=30.dp)
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState2)){
            MeetingField("Обсуждение 1", "18:00")
            Spacer(modifier = Modifier.height(30.dp))
            MeetingField("Обсуждение 2", "19:00")
            Spacer(modifier = Modifier.height(30.dp))
            MeetingField("Обсуждение 3", "20:00")
            Spacer(modifier = Modifier.height(30.dp))
            MeetingField("Обсуждение 4", "21:00")
            Spacer(modifier = Modifier.height(130.dp))
        }
    }

}

@Preview
@Composable
fun Show5() {
    ScheduleScreen()
}