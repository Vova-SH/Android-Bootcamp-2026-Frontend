package ru.sicampus.bootcamp2026.ui.screens.schedule

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.root.nav.ItemsNav
import ru.sicampus.bootcamp2026.ui.root.theme.BlueMain
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


data class TestBookData(
    val id: Long = 0,
    val title: String = "",
    val address: String = "",
    val description: String = "",
    val organizerId: Long = 0,
    val organizerName: String = "",
    val date: Long? = null,
    val timeStart: String = "",
    val timeEnd: String = ""
)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen(modifier: Modifier = Modifier,
                   navHostController: NavHostController) {
    val listBooks = remember {
        mutableStateOf(
            listOf<TestBookData?>(
                TestBookData(
                    title = "Планирование спринта",
                    address = "Зал 3",
                    description = "Обсуждение задач на следующую неделю.",
                    organizerName = "Gena",
                    timeStart = "10:00",
                    timeEnd = "11:00"
                )
            )
        )
    }
    Box(Modifier
        .fillMaxSize()
        .background(Color(0xffEEEEEE))) {
        IconButton(onClick = {
            navHostController.navigate(ItemsNav.BottomNavItems[3].route)
        }, colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color(0xff155DFC)
        ), modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp).size(54.dp).zIndex(1f)) {
            Icon(painterResource(R.drawable.plus),
                "", tint = Color.White)
        }
        Column() {
            Box(Modifier
                .fillMaxWidth()
                .background(Color.White)) {
                val currentDate = remember { mutableStateOf(LocalDate.now()) }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DatePick(currentDate)
                    var selectedIndex by remember { mutableIntStateOf(0) }
                    val options = listOf(stringResource(R.string.day), stringResource(R.string.week),
                        stringResource(R.string.month))
                    Spacer(Modifier.size(10.dp))
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        options.forEachIndexed { index, label ->
                            SegmentedButton(
                                modifier =
                                    Modifier,
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = options.size
                                ),
                                onClick = { selectedIndex = index },
                                selected = index == selectedIndex,
                                label = { Text(label) },
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = Color(0xff155DFC).copy(0.7f),
                                    activeContentColor = Color.White,
                                    inactiveContainerColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.size(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(listBooks.value) {
                    Card(modifier = Modifier
                        .fillMaxWidth(0.9f),
                        colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(Modifier.fillMaxSize()) {
                            Box(
                                Modifier
                                    .width(15.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 15.dp,
                                            bottomStart = 15.dp
                                        )
                                    )
                                    .zIndex(1f)
                                    .height(135.dp)
                                    .background(BlueMain.copy(0.7f))
                            )

                            Column(
                                Modifier.padding(
                                    top = 15.dp,
                                    start = 10.dp,
                                    end = 10.dp,
                                    bottom = 10.dp
                                )
                            ) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        it?.title ?: "Indefinite",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        it?.timeStart ?: "Indefinite",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black.copy(0.5f)
                                    )
                                }
                                Spacer(Modifier.size(5.dp))
                                Text(
                                    it?.description ?: "Indefinite",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black.copy(0.5f)
                                )
                                Spacer(Modifier.size(36.dp))
                                Row() {
                                    Box(Modifier.clip(RoundedCornerShape(15.dp)).background(
                                        color = Color(0xffEFF6FF)
                                    )) {
                                        Row() {
                                            Icon(
                                                painterResource(R.drawable.clock), "",
                                                Modifier.size(24.dp).padding(start = 2.dp, end = 2.dp,
                                                    top = 4.dp, bottom = 4.dp),
                                                tint = Color(0xff6151E8)
                                            )
                                            Text("${it?.timeStart}-${it?.timeEnd}", color = Color(0xff6151E8),
                                                fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, end = 8.dp))
                                        }
                                    }
                                    Spacer(Modifier.size(12.dp))
                                    Box(Modifier.clip(RoundedCornerShape(15.dp)).background(
                                        color = Color(0xffDEDEDE)
                                    )) {
                                        Row() {
                                            Icon(
                                                painterResource(R.drawable.location), "",
                                                Modifier.size(24.dp).padding(start = 1.dp, end = 1.dp,
                                                    top = 4.dp, bottom = 4.dp)
                                            )
                                            Text(it?.address ?:"Undefinite", color = Color.Black,
                                                fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, end = 8.dp))
                                        }
                                    }
                                }


                            }

                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePick(currentDate: MutableState<LocalDate>) {

    val minDate = LocalDate.now().minusDays(30)
    val maxDate = LocalDate.now().plusDays(30)
    val shortDays = remember {
        listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
    }
    Box(
        modifier = Modifier
            .height(45.dp)
            .fillMaxWidth(0.95f)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(color = Color(0xffF4F4F4))
    ) {
        Row() {
            IconButton(onClick = {
                currentDate.value = currentDate.value.minusDays(1)
            }, modifier = Modifier) {
                Icon(Icons.Default.KeyboardArrowLeft, "")
            }
            Spacer(Modifier.weight(1f))
            Text(
                "${
                    currentDate.value.dayOfWeek
                        .getDisplayName(TextStyle.SHORT, Locale("ru"))
                        .replaceFirstChar { it.uppercase() }
                }, ${currentDate.value.dayOfMonth} ${
                    currentDate.value.month.getDisplayName(
                        TextStyle.FULL, Locale("ru")
                    )
                }", fontSize = 16.sp, modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {
                currentDate.value = currentDate.value.plusDays(1)
            }, modifier = Modifier) {
                Icon(Icons.Default.KeyboardArrowRight, "")
            }
        }
    }
}

