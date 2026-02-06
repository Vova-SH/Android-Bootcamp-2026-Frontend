package ru.sicampus.bootcamp2026.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.sicampus.bootcamp2026.domain.model.MeetingStatus

val FilterGreenLight = Color(0xFFBBDBA6)

@Composable
fun HomeFilterDialog(
    onDismiss: () -> Unit,
    onApply: (String?) -> Unit
) {
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    val statuses = listOf(
        null to "Все встречи",
        MeetingStatus.SCHEDULED.name to "Запланированные",
        MeetingStatus.COMPLETED.name to "Завершенные",
        MeetingStatus.CANCELLED.name to "Отмененные"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF2A2A2A).copy(alpha = 0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Фильтр по статусу",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(statuses) { (status, label) ->
                        FilterStatusItem(
                            label = label,
                            isSelected = selectedStatus == status,
                            onClick = { selectedStatus = status }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Отмена", color = Color.White)
                    }

                    Button(
                        onClick = { onApply(selectedStatus) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FilterGreenLight
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Применить", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterStatusItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) FilterGreenLight.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Выбрано",
                    tint = FilterGreenLight,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}