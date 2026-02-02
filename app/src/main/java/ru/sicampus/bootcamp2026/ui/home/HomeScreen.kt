package ru.sicampus.bootcamp2026.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.components.HomeMeetingCard
import ru.sicampus.bootcamp2026.ui.components.HomeFilterDialog

val GreenLight = Color(0xFFBBDBA6)

@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    // Локальное состояние UI
    var showFilterDialog by remember { mutableStateOf(false) }
    var isSortExpanded by remember { mutableStateOf(false) }
    var sortLabel by remember { mutableStateOf("Decreasing") }

    val sidePadding = 24.dp

    // 1. Корневой контейнер
    Box(modifier = Modifier.fillMaxSize()) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            //Хедер
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good morning,",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                    Text(
                        text = "Rodion",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = GreenLight
                    )
                }
                IconButton(
                    onClick = onNavigateToProfile,
                    modifier = Modifier.size(56.dp)
                ) {
                    Surface(shape = CircleShape, color = Color.Gray) {
                        // Заглушка аватарки
                        Icon(Icons.Default.Person, null, modifier = Modifier.padding(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка создания новой встречи
            Button(
                onClick = onNavigateToCreate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding)
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Create new meeting",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            //Секция приглашений
            Text(
                text = "My invitations",
                color = GreenLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = sidePadding),
                fontSize = 24.sp,
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = sidePadding)
            ) {
                items(5) {
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Discussion about design...", color = Color.White) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFF2A2A2A).copy(alpha = 0.6f)
                        ),
                        border = null,
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Панель фильтров и сортировки
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding)
                    .height(64.dp),
                color = Color(0xFF2A2A2A).copy(alpha = 0.6f),
                shape = RoundedCornerShape(48.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Кнопка Фильтра
                    FilledIconButton(
                        onClick = { showFilterDialog = true },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = GreenLight),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = "Filter",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Выпадающий список сортировки
                    Box {
                        Button(
                            onClick = { isSortExpanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(sortLabel, color = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowDropDown, null, tint = Color.Black)
                        }

                        DropdownMenu(
                            expanded = isSortExpanded,
                            onDismissRequest = { isSortExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Increasing") },
                                onClick = { sortLabel = "Increasing"; isSortExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Decreasing") },
                                onClick = { sortLabel = "Decreasing"; isSortExpanded = false }
                            )
                        }
                    }
                }
            }

            // Диалог фильтров (показывается поверх всего, если showFilterDialog == true)
            if (showFilterDialog) {
                HomeFilterDialog(
                    onDismiss = { showFilterDialog = false },
                    onApply = { showFilterDialog = false }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Список встреч
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 16.dp), // Небольшой отступ снизу
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(10) { index ->
                    Box(modifier = Modifier.padding(horizontal = sidePadding)) {
                        HomeMeetingCard(
                            title = "Meeting regarding the project #${index + 1}",
                            date = "25, Tue",
                            startTime = "15:00",
                            endTime = "17:00",
                            participantsCount = 4 + index,
                            onClick = { onNavigateToDetails("id_$index") }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToCreate = {},
        onNavigateToDetails = {},
        onNavigateToProfile = {}
    )
}