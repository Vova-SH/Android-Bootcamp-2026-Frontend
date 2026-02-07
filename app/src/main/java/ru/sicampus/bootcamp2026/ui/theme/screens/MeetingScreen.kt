package ru.sicampus.bootcamp2026.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun ColleaguesScreen() {

    val colleagues = listOf(
        Colleague("ФИО должность, отдел", "7 9876543210"),
        Colleague("ФИО должность, отдел", "7 9876543211"),
        Colleague("ФИО должность, отдел", "7 9876543212"),
        Colleague("ФИО должность, отдел", "7 9876543213"),
        Colleague("ФИО должность, отдел", "7 9876543214")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Сотрудники",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(colleagues) { colleague ->
                ColleagueItem(colleague = colleague)
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 5.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}


data class Colleague(
    val name: String,
    val phone: String
)

@Composable
fun ColleagueItem(colleague: Colleague) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = colleague.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = colleague.phone,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )


        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0057B8)
            )
        ) {
            Text(
                text = "Пригласить",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

