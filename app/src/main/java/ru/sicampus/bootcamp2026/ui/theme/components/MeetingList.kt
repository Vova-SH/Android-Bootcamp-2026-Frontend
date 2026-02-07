package ru.sicampus.bootcamp2026.ui.theme.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.DeepBlue
import ru.sicampus.bootcamp2026.ui.theme.Typography

@Composable
fun MeetingListItem(
    meetingName: String,
    dateAndTime: String
) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 20.dp).fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(start = 6.dp)
        ) {
            Text(
                meetingName,
                Modifier.padding(bottom = 5.dp),
                style = Typography.bodyLarge
            )
            Text(
                dateAndTime,
                style = Typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Информация о встрече"
            )
        }
    }
}

@Composable
fun MeetingList(
    meetingNames: List<String>,
    datesAndTimes: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.height(548.dp)) {
        itemsIndexed(meetingNames) { index, meetingName ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DeepBlue,
                ),
                shape = RoundedCornerShape(20.dp),
            ) {
                if (meetingNames.isNotEmpty() && datesAndTimes.isNotEmpty()) {
                    MeetingListItem(
                        meetingName = meetingName,
                        dateAndTime = datesAndTimes[index]
                    )
                } else {
                    Text("Нет встреч")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMeetingList() {
    val meetingNames : List<String> = listOf("Анна", "Борис", "Василий")
    val datesAndTimes : List<String> = listOf("123124", "45745754", "465")
    AndroidBootcamp2026FrontendTheme() {
        MeetingList(meetingNames, datesAndTimes)
    }
}