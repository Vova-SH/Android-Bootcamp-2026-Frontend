package ru.sicampus.bootcamp2026.ui.screens.incomingbooks

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import ru.sicampus.bootcamp2026.utils.TimeUtils
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.sicampus.bootcamp2026.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomingScreen(
    context: Context,
    viewModel: IncomingViewModel = viewModel(factory = IncomingViewModelFactory.create(context))
) {

    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.White)
            ) {
                Text(
                    stringResource(R.string.incoming),
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        modifier = Modifier,
        containerColor = Color(0xffEEEEEE)
    ) { it1 ->
        when {
            !viewModel.state.collectAsState().value.isLoading -> {
                val listIncoming = viewModel.state.collectAsState().value.invitations
                var isRefreshing by remember { mutableStateOf(false) }
                val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { viewModel.loadInvitations() },
                ) {
                    LazyColumn(
                        Modifier
                            .padding(top = (it1.calculateTopPadding().value + 20).dp)
                            .fillMaxWidth()
                    ) {
                        items(listIncoming) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.94f)
                                        .clip(
                                            RoundedCornerShape(20.dp)
                                        )
                                        .background(Color.White)
                                        .align(Alignment.Center)
                                ) {
                                    Column {
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(
                                                Modifier.padding(
                                                    start = 18.dp,
                                                    top = 10.dp
                                                )
                                            ) {
                                                Text(
                                                    if (it.title.length > 22) it.title.take(22) + "..." else it.title,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 20.sp,
                                                )
                                                Text(
                                                    "От ${it.authorFirstName} ${it.authorSecondName}",
                                                    color = Color(0xff636363),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Row(Modifier.padding(top = 10.dp)) {
                                                    Icon(
                                                        painterResource(R.drawable.clock),
                                                        "",
                                                        tint = Color(0xff1480F0),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(Modifier.size(3.dp))
                                                    Text(
                                                        "${TimeUtils.timeHourMinutes(it.timeStart)}-${TimeUtils.timeHourMinutes(it.timeEnd)}",
                                                        modifier = Modifier.align(Alignment.CenterVertically)
                                                    )
                                                    Spacer(Modifier.size(11.dp))
                                                    Icon(
                                                        painterResource(R.drawable.location),
                                                        "",
                                                        tint = Color(0xff1480F0),
                                                        modifier = Modifier.size(21.dp)
                                                    )
                                                    Spacer(Modifier.size(2.dp))
                                                    Text(
                                                        it.address,
                                                        modifier = Modifier.align(Alignment.CenterVertically)
                                                    )
                                                }

                                            }
                                            Text(
                                                getDisplayDateIncome(ZonedDateTime.of(it.date.atStartOfDay(), ZoneId.systemDefault()).toInstant().toEpochMilli()),
                                                fontSize = 16.sp,
                                                color = Color(0xff636363).copy(0.8f),
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(
                                                    end = 20.dp,
                                                    top = 15.dp
                                                )
                                            )

                                        }
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    start = 10.dp,
                                                    top = 20.dp,
                                                    end = 10.dp
                                                )
                                        ) {
                                            Button(
                                                onClick = {
                                                    viewModel.respondToInvitation(it.id, true)
                                                },
                                                modifier = Modifier
                                                    .height(35.dp)
                                                    .weight(1f),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xff155DFC)
                                                )
                                            ) {
                                                Text(stringResource(R.string.accept))
                                            }
                                            Spacer(Modifier.size(10.dp))
                                            Button(
                                                onClick = {
                                                    viewModel.respondToInvitation(it.id, false)
                                                },
                                                shape = RoundedCornerShape(16.dp),
                                                modifier = Modifier
                                                    .height(35.dp)
                                                    .weight(1f),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xffE6E6E6)
                                                )
                                            ) {
                                                Text(
                                                    stringResource(R.string.reject),
                                                    color = Color.Black,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }
                                        Spacer(Modifier.size(16.dp))

                                    }
                                }
                            }
                            Spacer(Modifier.size(16.dp))
                        }
                    }
                }
            }

            else -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = Color(0xFF5D5D73))
                }
            }
        }

    }

}

fun getDisplayDateIncome(l: Long): String {
    if (Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDate() ==
        LocalDate.now()
    ) {
        return "Сегодня"
    } else if (
        dateToLong(Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDate()) -
        dateToLong(LocalDate.now()) == 1.toLong()
    ) {
        return "Завтра"
    }
    return "${
        Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDate().format(
            DateTimeFormatter.ofPattern("d MMM", Locale("ru"))
        )
    }"
}

fun dateToLong(date: LocalDate): Long {
    return "${date.year}${date.monthValue}${date.dayOfMonth}".toLong()
}
