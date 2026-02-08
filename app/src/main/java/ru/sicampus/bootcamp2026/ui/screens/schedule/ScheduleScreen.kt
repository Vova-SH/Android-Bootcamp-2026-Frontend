package ru.sicampus.bootcamp2026.ui.screens.schedule
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.chouaibmo.rowkalendar.RowKalendar
import io.github.chouaibmo.rowkalendar.components.DateCellDefaults
import io.github.chouaibmo.rowkalendar.extensions.isBefore
import io.github.chouaibmo.rowkalendar.extensions.now
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.components.MeetingField
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.White
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
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
            shape = RoundedCornerShape(
                bottomStart = 30.dp,
                bottomEnd = 30.dp
            )
        )
        {
            Text(
                text = "${LocalDate.now().month.toString().lowercase().replaceFirstChar
                { it.uppercase() }}, ${LocalDate.now().year}",
                fontSize = 30.sp,
                color = Black,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                modifier = Modifier.padding(horizontal = 24.dp)
                    .padding(top = 60.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            RowKalendarSample()

        }

        Text(
            text = "Встречи",
            fontSize = 20.sp,
            color = Black,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.padding(horizontal = 24.dp)
                .padding(top = 30.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            item {
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

@Composable
fun RowKalendarSample() {
    RowKalendar(
        modifier = Modifier.fillMaxSize(),
        content = { date, isSelected, onClick ->
            Cell(
                date = date,
                isSelected = isSelected,
                onDateSelected = onClick,
                shape = RoundedCornerShape(20.dp),
                elevation = DateCellDefaults.DateCellElevation(
                    selectedElevation = 0.dp,
                    pastElevation = 0.dp,
                    futureElevation = 0.dp
                ),
                colors = DateCellDefaults.colors(
                    selectedContainerColor = Blue,
                    selectedTextColor = White,
                    pastContainerColor = Color.Transparent,
                    pastTextColor = Black,
                    futureContainerColor = Color.Transparent,
                    futureTextColor = Black
                ),
                modifier = Modifier.height(100.dp)
                    .width(90.dp)
                    .padding(horizontal = 8.dp)

            )
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScheduleScreen()
}

@Composable
fun Cell(
    modifier: Modifier = Modifier,
    date: kotlinx.datetime.LocalDate,
    isSelected: Boolean = false,
    onDateSelected: (kotlinx.datetime.LocalDate) -> Unit,
    shape: Shape = DateCellDefaults.shape,
    colors: DateCellDefaults.DateCellColors = DateCellDefaults.colors(),
    elevation: DateCellDefaults.DateCellElevation = DateCellDefaults.elevation(),
    border: DateCellDefaults.DateCellBorder? = null
) {

    val cellColor = when {
        isSelected -> colors.selectedContainerColor
        date.isBefore(kotlinx.datetime.LocalDate.now()) -> colors.pastContainerColor
        else -> colors.futureContainerColor
    }

    val textColor = when {
        isSelected -> colors.selectedTextColor
        date.isBefore(kotlinx.datetime.LocalDate.now()) -> colors.pastTextColor
        else -> colors.futureTextColor
    }

    val cellBorder = border?.let {
        when {
            isSelected -> BorderStroke(it.selectedBorderWidth, it.selectedBorderColor)
            date.isBefore(kotlinx.datetime.LocalDate.now()) -> BorderStroke(it.pastBorderWidth, it.pastBorderColor)
            else -> BorderStroke(it.futureBorderWidth, it.futureBorderColor)
        }
    }

    val cellElevation = when {
        isSelected -> elevation.selectedElevation
        date.isBefore(kotlinx.datetime.LocalDate.now()) -> elevation.pastElevation
        else -> elevation.futureElevation
    }

    Card(
        modifier = modifier
            .height(200.dp)
            .width(100.dp)
            .shadow(elevation = cellElevation, shape = shape)
            .clip(shape = shape)
            .clickable { onDateSelected(date) },
        border = cellBorder,
        colors = CardDefaults.cardColors(containerColor = cellColor),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 17.dp).padding(start = 15.dp)
        ) {
            Text(
                text = date.dayOfWeek.name.subSequence(0, 3).toString()
                    .lowercase()
                    .replaceFirstChar { it.uppercase() },
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontFamily = (FontFamily(Font(R.font.montserrat_regular))),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = date.dayOfMonth.toString(),
                fontWeight = FontWeight.Black,
                color = textColor,
                fontSize = 24.sp,
                fontFamily = (FontFamily(Font(R.font.montserrat_semibold)))
            )
        }
    }
}