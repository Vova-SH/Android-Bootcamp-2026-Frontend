package ru.sicampus.bootcamp2026.ui.screens.profile


import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.login.LoginActivity
import ru.sicampus.bootcamp2026.ui.root.theme.Typography
import ru.sicampus.bootcamp2026.utils.SettingsUtils

@Composable
fun ProfileScreen(modifier: Modifier = Modifier,
                  context: Context) {
    val settingsUtils = SettingsUtils(App.context)

    val searchText = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                OutlinedTextField(
                    leadingIcon = { Icon(Icons.Outlined.Search, "") },
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    label = {
                        Text(
                            stringResource(R.string.search), style =
                                Typography.bodyLarge
                        )
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.zIndex(1f).fillMaxWidth(0.95f)
                )
            LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Spacer(Modifier.size(20.dp))
                Box(
                    Modifier.size(155.dp).background(
                        color = Color(0xff54E68C), shape = CircleShape
                    ), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(R.drawable.profile), "", Modifier.size(130.dp),
                        tint = Color.White
                    )

                }
                Spacer(Modifier.size(5.dp))
                Text(
                    "Алексей Петров", fontSize = 20.sp, color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    "alexpetrov@bk.ru", fontSize = 15.sp, color = Color(0xff636363),
                    fontWeight = FontWeight.Normal
                )
                Box(
                    Modifier

                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 20.dp)
                        .shadow(4.dp, RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.White)
                ) {
                    Column() {
                        Column(Modifier.padding(top = 15.dp, start = 25.dp, bottom = 10.dp)) {
                            Text(
                                "Frontend-Разработчик", fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                stringResource(R.string.post), fontSize = 14.sp, color = Color(0xff636363),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.size(4.dp))
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                        Column(Modifier.padding(top = 15.dp, start = 25.dp, bottom = 10.dp)) {
                            Text(
                                "Разработка продуктов", fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                stringResource(R.string.department), fontSize = 14.sp, color = Color(0xff636363),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.size(4.dp))
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                            Spacer(Modifier.size(10.dp))
                            Text(
                                "Я — Frontend-разработчик. \n" +
                                        "Моя главная задача — превращать идеи \n" +
                                        "дизайнеров и требования бизнеса в \n" +
                                        "быстрые, удобные и красивые интерфейсы, с которыми взаимодействуют наши \n" +
                                        "пользователи.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                stringResource(R.string.about),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xff636363)
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                        }
                    }
                }
                Box(
                    modifier = Modifier.height(50.dp).fillMaxWidth(0.95f)
                        .clip(RoundedCornerShape(15.dp))
                        .border(1.dp, color = Color(0xff2F458B), shape = RoundedCornerShape(16.dp))
                        .clickable {}.background(
                            Color.White
                        )
                ) {
                    Text(
                        stringResource(R.string.edit),
                        color = Color(0xff0A266C),
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.size(15.dp))
                Card(
                    onClick = {
                        SettingsUtils(context = App.context).clear()
                        context.startActivity(
                            Intent(context, LoginActivity::class.java)
                        )

                    }, modifier = Modifier.height(50.dp).fillMaxWidth(0.95f).clip(
                        RoundedCornerShape(15.dp)
                    ), colors = CardDefaults.cardColors(containerColor = Color(0xffFFBBBB))
                ) {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(R.drawable.exit_icon), "",
                            tint = Color(0xffD50000))
                        Spacer(Modifier.size(5.dp))
                        Text(
                            stringResource(R.string.logout), color = Color(0xffD50000),
                            style = Typography.bodyLarge, fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(Modifier.size(20.dp))
            }
            }

        }}
}


