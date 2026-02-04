package ru.sicampus.bootcamp2026.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.ui.theme.LightGreen

/*
TODO: реализовать переход на экран встречи по клику на карточку
  реализовать динамическое получение данных из ViewModel
 */
@Composable
fun HomeMeetingCard(
    title: String,
    date: String,
    startTime: String,
    endTime: String,
    participantsCount: Int,
    onClick: () -> Unit
){
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(48.dp),
        colors = CardDefaults.cardColors(containerColor = LightGreen)
    ){
        Column(modifier = Modifier.padding(25.dp)) {
            //Заголовок
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 2
                )

            Spacer(modifier = Modifier.height(12.dp))

            //разделитель
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                Column {
                    Text(text = "Day: $date", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Time: $startTime - $endTime", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Participants: $participantsCount", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                }

                // Стрелочка
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeMeetingCardPreview() {
    HomeMeetingCard(
        title = "Project Kickoff Meeting",
        date = "2024-07-01",
        startTime = "10:00 AM",
        endTime = "11:00 AM",
        participantsCount = 5,
        onClick = {}
    )
}