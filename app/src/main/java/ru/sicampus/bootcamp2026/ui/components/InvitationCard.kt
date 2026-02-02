package ru.sicampus.bootcamp2026.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkGrey

@Composable
fun InvitationCard(
    name: String,
    startTime: Byte,
    endTime: Byte,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),

        colors = CardColors(Color(0xFFDDDDDD),
            Color(0xFFDDDDDD),
            Color(0xFFDDDDDD),
            Color(0xFFDDDDDD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Blue,
                    shape = RoundedCornerShape(20),

                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .padding(6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.hand),
                        contentDescription = "Hand",
                        modifier = Modifier
                            .padding(3.dp)
                    )
                }

                Text(
                    text = name,
                    color = Black,
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                )

                Text(
                    text = "$startTime:$endTime",
                    color = DarkGrey,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    textAlign = TextAlign.End
                )
            }

            Row(
                modifier = Modifier,

                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Принять",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                    color = Blue,
                    textAlign = TextAlign.Center,

                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp)
                )


                Text(
                    text = "Отказаться",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                    color = Blue,
                    textAlign = TextAlign.Center,

                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp)
                )
            }
        }

    }
}
