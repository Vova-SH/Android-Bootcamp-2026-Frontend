package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.components.UserField
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkGrey
import ru.sicampus.bootcamp2026.ui.theme.LightGrey
import ru.sicampus.bootcamp2026.ui.theme.White

@Composable
fun CreateInviteScreen() {
    var isEditable by remember { mutableStateOf(true) }

    Box(modifier = Modifier.padding()) {
        var meetingName by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Создайте встречу",
                color = Color.Black,
                fontFamily = FontFamily(androidx.
                compose.ui.text.font.Font(R.font.montserrat_bold)),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(top = 5.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                UserField(
                    "Название встречи","Введите название встречи", "", isEditable = isEditable,
                    R.drawable.name
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { },
                    shape = RoundedCornerShape(32),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(63.dp)
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightGrey,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            contentDescription = "",
                            tint = DarkGrey,

                            )
                        Text(
                            text = "Забронировать время",
                            modifier = Modifier
                                .padding(start = 13.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { },
                    shape = RoundedCornerShape(32),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(63.dp)
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightGrey,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.people),
                            contentDescription = "",
                            tint = DarkGrey,

                            )
                        Text(
                            text = "Выбрать участников",
                            modifier = Modifier
                                .padding(start = 13.dp)
                        )
                    }


                }

                Spacer(modifier = Modifier.height(90.dp))

                Button(onClick = {},
                    modifier = Modifier
                        .height(63.dp)
                        .width(170.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(Blue),
                ) {
                    Text(text="Создать", color= White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(R.font.montserrat_bold)))
                }
            }
        }
    }
}