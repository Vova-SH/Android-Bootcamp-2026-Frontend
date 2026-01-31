package ru.sicampus.bootcamp2026.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.screens.ScheduleScreen
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkGrey
import ru.sicampus.bootcamp2026.ui.theme.LightGrey
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun MeetingField(
    label: String,
    time: String
) {

    Card(colors = CardDefaults.cardColors(
        containerColor = LightGrey
    ),
        modifier = Modifier.fillMaxWidth().height(100.dp)
        .clip(RoundedCornerShape(20.dp))) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            MeetIcon()
            Text(label, fontSize = 18.sp, fontFamily = FontFamily(androidx.
            compose.ui.text.font.Font(R.font.montserrat_bold)),
                color = Black,
                modifier = Modifier
                .padding(horizontal = 16.dp))
            Text(time, fontSize = 12.sp, fontFamily = FontFamily(androidx.
            compose.ui.text.font.Font(R.font.montserrat_regular)),
                color = DarkGrey,
                modifier = Modifier
                    .padding(start=18.dp))

        }
    }

}
