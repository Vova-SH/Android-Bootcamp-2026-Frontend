package ru.sicampus.bootcamp2026.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.NavRoutes
import ru.sicampus.bootcamp2026.components.CustomSearchBar
import ru.sicampus.bootcamp2026.currentUser
import ru.sicampus.bootcamp2026.data.MeetingData
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// Состояния сортировки
enum class MeetingsSortState {
    TIME_ASC,      // По времени (раньше → позже)
    TIME_DESC,     // По времени (позже → раньше)
    NAME_ASC,      // По алфавиту (А-Я)
    NAME_DESC,     // По алфавиту (Я-А)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    meetings: List<MeetingData>
) {
    currentUser?.let { currentUser ->
        var showForMe by remember { mutableStateOf(true) }
        var showFromMe by remember { mutableStateOf(false) }
        var searchQuery by remember { mutableStateOf("") }
        var searchType by remember { mutableStateOf("Название") }
        var sortState by remember { mutableStateOf(MeetingsSortState.TIME_ASC) }
        var searchHistory by remember { mutableStateOf<List<String>>(emptyList()) }

        // Опции поиска для встреч
        val searchOptions = listOf("Название", "Участник", "Время")

        // Фильтрация встреч на основе поиска и выбранных фильтров
        val filteredMeetings = remember(meetings, searchQuery, searchType, showForMe, showFromMe, currentUser) {
            if (searchQuery.isEmpty()) {
                meetings.filter { meeting ->
                    when {
                        showForMe -> currentUser in meeting.users
                        showFromMe -> currentUser in meeting.admins || currentUser == meeting.creator
                        else -> false
                    }
                }
            } else {
                meetings.filter { meeting ->
                    val matchesSearch = when (searchType) {
                        "Название" -> meeting.name.contains(searchQuery, ignoreCase = true)
                        "Участник" -> {
                            meeting.users.keys.any { user ->
                                (user.name.contains(searchQuery, ignoreCase = true) ||
                                        user.surname.contains(searchQuery, ignoreCase = true) ||
                                        user.patronymic?.contains(searchQuery, ignoreCase = true) ?: false)
                            }
                        }
                        "Время" -> {
                            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
                            val formattedTime = meeting.time.format(formatter)
                            formattedTime.contains(searchQuery, ignoreCase = true) ||
                                    meeting.time.toString().contains(searchQuery, ignoreCase = true)
                        }
                        else -> true
                    }

                    val matchesFilter = when {
                        showForMe -> currentUser in meeting.users
                        showFromMe -> currentUser in meeting.admins || currentUser == meeting.creator
                        else -> false
                    }

                    matchesSearch && matchesFilter
                }
            }
        }

        // Применение сортировки
        val sortedMeetings = remember(filteredMeetings, sortState) {
            when (sortState) {
                MeetingsSortState.TIME_ASC -> filteredMeetings.sortedBy { it.time }
                MeetingsSortState.TIME_DESC -> filteredMeetings.sortedByDescending { it.time }
                MeetingsSortState.NAME_ASC -> filteredMeetings.sortedBy { it.name }
                MeetingsSortState.NAME_DESC -> filteredMeetings.sortedByDescending { it.name }
            }
        }

        // Иконка и описание для сортировки
        val (sortIcon, sortContentDescription) = remember(sortState) {
            when (sortState) {
                MeetingsSortState.TIME_ASC -> Pair(
                    Icons.Default.Schedule,
                    "Сортировка по времени (раньше → позже)"
                )
                MeetingsSortState.TIME_DESC -> Pair(
                    Icons.Default.Schedule,
                    "Сортировка по времени (позже → раньше)"
                )
                MeetingsSortState.NAME_ASC -> Pair(
                    Icons.Default.SortByAlpha,
                    "Сортировка по алфавиту (А-Я)"
                )
                MeetingsSortState.NAME_DESC -> Pair(
                    Icons.Default.SortByAlpha,
                    "Сортировка по алфавиту (Я-А)"
                )
            }
        }

        // Циклическое переключение состояний сортировки
        val onSortClick = {
            sortState = when (sortState) {
                MeetingsSortState.TIME_ASC -> MeetingsSortState.TIME_DESC
                MeetingsSortState.TIME_DESC -> MeetingsSortState.NAME_ASC
                MeetingsSortState.NAME_ASC -> MeetingsSortState.NAME_DESC
                MeetingsSortState.NAME_DESC -> MeetingsSortState.TIME_ASC
            }
        }

        // Добавление в историю поиска
        val addToHistory = { query: String ->
            if (query.isNotBlank() && query !in searchHistory) {
                searchHistory = (searchHistory + query).takeLast(10)
            }
        }

        // Очистка истории поиска
        val clearHistory = {
            searchHistory = emptyList()
        }

        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomSearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = {
                        searchQuery = it
                    },
                    searchType = searchType,
                    onSearchTypeChange = {
                        searchType = it
                        // При смене типа поиска добавляем текущий запрос в историю
                        if (searchQuery.isNotBlank()) {
                            addToHistory(searchQuery)
                        }
                    },
                    searchOptions = searchOptions,
                    placeholder = when (searchType) {
                        "Название" -> "Поиск по названию..."
                        "Участник" -> "Поиск по участнику..."
                        "Время" -> "Поиск по времени..."
                        else -> "Поиск..."
                    },
                    onClearClick = {
                        searchQuery = ""
                    },
                    onSearchAction = {
                        addToHistory(searchQuery)
                    },
                    onSortClick = onSortClick,
                    sortIcon = sortIcon,
                    sortContentDescription = sortContentDescription,
                    searchHistory = searchHistory,
                    onHistoryItemClick = { historyItem ->
                        searchQuery = historyItem
                    },
                    onAddToHistory = { query ->
                        addToHistory(query)
                    },
                    onClearHistory = clearHistory, // Передаем функцию очистки
                    showHistory = true,
                    showHistoryOnFocus = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = showForMe,
                        onClick = {
                            showForMe = !showForMe
                            if (showForMe) showFromMe = false
                        },
                        label = { Text("Вам") },
                        trailingIcon = if (showForMe) {
                            {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FilterChip(
                        selected = showFromMe,
                        onClick = {
                            showFromMe = !showFromMe
                            if (showFromMe) showForMe = false
                        },
                        label = { Text("От вас") },
                        trailingIcon = if (showFromMe) {
                            {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else null
                    )
                }

                // Индикатор текущей сортировки
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Сортировка: ${when (sortState) {
                            MeetingsSortState.TIME_ASC -> "по времени (раньше → позже)"
                            MeetingsSortState.TIME_DESC -> "по времени (позже → раньше)"
                            MeetingsSortState.NAME_ASC -> "по алфавиту (А-Я)"
                            MeetingsSortState.NAME_DESC -> "по алфавиту (Я-А)"
                        }}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (sortedMeetings.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (searchQuery.isEmpty()) {
                                    "Нет встреч"
                                } else {
                                    "Ничего не найдено"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (searchQuery.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "По запросу \"$searchQuery\" в категории \"$searchType\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        item {
                            Text(
                                text = "Найдено встреч: ${sortedMeetings.size}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(sortedMeetings) { meeting ->
                            meeting.ClassicCard(currentUser, Modifier.clickable { /* TODO сделать переход на экран встречи (это я уж сам доделаю) */ })
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate(NavRoutes.CreateMeeting.route) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Создать встречу"
                )
            }
        }
    }
}