package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun TodayField(
    number: Int,
    dayWeek: String
) {
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
}