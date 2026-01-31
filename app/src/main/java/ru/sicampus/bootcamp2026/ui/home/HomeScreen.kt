package ru.sicampus.bootcamp2026.ui.home

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.components.HomeBottomBar
import ru.sicampus.bootcamp2026.ui.components.HomeMeetingCard
import ru.sicampus.bootcamp2026.R

val GreenLight = Color(0xFFBBDBA6)

@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    currentTab: Int,
    onTabSelected: (Int) -> Unit
) {
    var isSortExpanded by remember { mutableStateOf(false) }
    var sortLabel by remember { mutableStateOf("Decreasing") }
    val sidePadding = 24.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Фон
        Image(
            painter = painterResource(id = R.drawable.green_gradient),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            //минус системные отступы Scaffold, чтобы контент был во весь экран
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                HomeBottomBar(currentTab = currentTab, onTabSelected = onTabSelected)
            }
        ) { paddingValues -> // игнор

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                //Отступ только под системные часы
                Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

                //Небольшой отступ от часов до контента
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
                        Text("Good morning,", color = Color.White.copy(alpha = 0.7f))
                        Text(
                            text = "Rodion",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = GreenLight
                        )
                    }
                    IconButton(onClick = onNavigateToProfile, modifier = Modifier.size(56.dp)) {
                        Surface(shape = CircleShape, color = Color.Gray) {
                            Icon(Icons.Default.Person, null, modifier = Modifier.padding(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.height(16.dp))

                // My invitations
                Text(
                    text = "My invitations",
                    color = GreenLight,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = sidePadding)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = sidePadding)
                ) {
                    items(5) { _ ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text("Discussion about design...", color = Color.White) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFF2A2A2A).copy(alpha = 0.6f)),
                            border = null,
                            shape = RoundedCornerShape(50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Фильтры (Подложка)
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
                        FilledIconButton(
                            onClick = { },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = GreenLight),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }

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
                            // Меню
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

                Spacer(modifier = Modifier.height(16.dp))

                // список запланированных встреч
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(
                        bottom = 80.dp,
                        top = 0.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(10) { index ->
                        Box(modifier = Modifier.padding(horizontal = sidePadding)) {
                            HomeMeetingCard(
                                title = "Meeting regarding the project #${index + 1}",
                                date = "25, Tue",
                                startTime = "17:00",
                                endTime = "18:00",
                                participantsCount = 4 + index,
                                onClick = { onNavigateToDetails("id_$index") }
                            )
                        }
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
        onNavigateToProfile = {},
        currentTab = 1,
        onTabSelected = {})
}