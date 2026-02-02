package ru.sicampus.bootcamp2026.ui.screens.schedule

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.components.DayField
import ru.sicampus.bootcamp2026.ui.components.MeetingField
import ru.sicampus.bootcamp2026.ui.components.TodayField
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun ScheduleScreen() {

    Column(modifier = Modifier.fillMaxSize().background(White)) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            modifier = Modifier.fillMaxWidth()
                .height(320.dp)
                .dropShadow(
                    shape = RoundedCornerShape(20.dp),
                    shadow = Shadow(
                        radius = 15.dp,
                        spread = 0.dp,
                        color = Blue,
                        offset = DpOffset(x = 0.dp, y = 4.dp),
                        alpha = 0.3f
                    )
                ),
            shape = RoundedCornerShape(bottomStart = 30.dp,
                bottomEnd = 30.dp)
        )
        {
            Text(text = "Jan, 2026",
                fontSize = 30.sp,
                color = Black,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                modifier = Modifier.padding(horizontal = 24.dp)
                    .padding(top=60.dp)
            )
            LazyRow (
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top=47.dp),
            ) {
                item {TodayField(28, "Ср")
                    Spacer(modifier = Modifier.width(10.dp))
                }
                item {DayField(29, "Чт")
                    Spacer(modifier = Modifier.width(10.dp))
                }
                item {DayField(30, "Пт")
                    Spacer(modifier = Modifier.width(10.dp))
                }

                item { DayField(31, "Сб")
                    Spacer(modifier = Modifier.width(10.dp))
                }
                item {
                    DayField(1, "Вс")
                    Spacer(modifier = Modifier.width(10.dp))
                }
                item {
                    DayField(2, "Пн")
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }

        }

        Text(text = "Встречи",
            fontSize = 20.sp,
            color = Black,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.padding(horizontal = 24.dp)
                .padding(top=30.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            item{
                MeetingField("Обсуждение 1", "18:00")
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                MeetingField("Обсуждение 2", "19:00")
                Spacer(modifier = Modifier.height(30.dp))
            }

            item {
                MeetingField("Обсуждение 3", "20:00")
                Spacer(modifier = Modifier.height(30.dp))
            }

            item {
                MeetingField("Обсуждение 4", "21:00")
                Spacer(modifier = Modifier.height(130.dp))
            }
        }

    }

}

@Preview
@Composable
fun Show5() {
    ScheduleScreen()
}