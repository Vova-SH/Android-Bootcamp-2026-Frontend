package ru.sicampus.bootcamp2026.android.ui.testScreens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.android.ui.components.CustomButton
import ru.sicampus.bootcamp2026.android.ui.components.CustomNavigationBar
import ru.sicampus.bootcamp2026.android.ui.components.CustomTextField
import ru.sicampus.bootcamp2026.android.ui.theme.AppTheme
import ru.sicampus.bootcamp2026.android.ui.theme.Black
import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.android.ui.theme.Grey
import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey
import ru.sicampus.bootcamp2026.android.ui.theme.White

@Composable
fun CreateMeetingScreen(
    navController: NavHostController
) {
    var meetingName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var invite by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            CustomNavigationBar(
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(150.dp))

            Text(
                text = "Создание встречи",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.open_sans_bold))
            )

            Spacer(modifier = Modifier.height(38.dp))


            //  Название встречи
            CustomTextField(
                value = meetingName,
                onValueChange = { meetingName = it },
                label = "Название встречи",
                modifier = Modifier,
                TextGrey
            )

            Spacer(modifier = Modifier.height(10.dp))

            //  Описание
            CustomTextField(
                value = description,
                onValueChange = { description = it },
                label = "Описание",
                modifier = Modifier,
                TextGrey
            )

            Spacer(modifier = Modifier.height(60.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 137.dp, start = 27.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.calendar_icon),
                    contentDescription = "Дата встречи"
                )

                Spacer(modifier = Modifier.width(13.dp))

                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = TextGrey,
                            shape = RoundedCornerShape(40.dp)
                        )
                        .clickable {}
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "19.01.2029",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.open_sans_bold)),
                            color = Black,
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp, top = 3.dp, bottom = 3.dp)
                        )

                        Spacer(modifier = Modifier.width(34.dp))

                        Icon(
                            painter = painterResource(R.drawable.triangle_grey_icon),
                            contentDescription = "Открыть календарь",
                            modifier = Modifier
                                .padding(end = 14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(31.dp))

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 137.dp, start = 27.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.clock_icon),
                    contentDescription = "Дата встречи"
                )

                Spacer(modifier = Modifier.width(13.dp))

                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = TextGrey,
                            shape = RoundedCornerShape(40.dp)
                        )
                        .clickable {}
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "9:00 - 10:00",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.open_sans_bold)),
                            color = Black,
                            modifier = Modifier
                                .padding(start = 15.dp, end = 15.dp, top = 3.dp, bottom = 3.dp)
                        )

                        Spacer(modifier = Modifier.width(34.dp))

                        Icon(
                            painter = painterResource(R.drawable.triangle_grey_icon),
                            contentDescription = "Выбрать время",
                            modifier = Modifier
                                .padding(end = 14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            Box() {
                TextField(
                    value = invite,
                    onValueChange = { },
                    label = {
                        Text(
                            text = "Пригласить участников",
                            fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
                            color = TextGrey
                        )
                    },
                    modifier = Modifier
                        .padding(start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Black,
                        unfocusedTextColor = Black,
                        focusedContainerColor = Grey,
                        unfocusedContainerColor = Grey,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp)
                )

                Icon(
                    painter = painterResource(R.drawable.triangle_grey_icon),
                    contentDescription = "Пригласить участников",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 37.dp, bottom = 25.dp)
                )
            }

            Spacer(modifier = Modifier.height(52.dp))

            CustomButton(
                "Создать",
                DarkBlue,
                White,
                {},
                Modifier
                    .padding(start = 64.dp, end = 64.dp)
                    .fillMaxWidth()
                    .height(36.dp)
            )
        }


    }
}