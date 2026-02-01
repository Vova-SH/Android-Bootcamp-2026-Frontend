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
import ru.sicampus.bootcamp2026.data.UserData
import ru.sicampus.bootcamp2026.selectedUser

// Состояния сортировки для пользователей
enum class UsersSortState {
    NAME_ASC,      // По ФИО (А-Я)
    NAME_DESC,     // По ФИО (Я-А)
}

@Composable
fun UsersScreen(
    navController: NavHostController,
    users: List<UserData>,
    modifier: Modifier = Modifier
) {
    currentUser?.let { currentUser ->
        var showAll by remember { mutableStateOf(true) }
        var showFriends by remember { mutableStateOf(false) }

        // region search fun
        var searchQuery by remember { mutableStateOf("") }
        var searchType by remember { mutableStateOf("ФИО") }
        var sortState by remember { mutableStateOf(UsersSortState.NAME_ASC) }
        var searchHistory by remember { mutableStateOf<List<String>>(emptyList()) }
        val searchOptions = listOf("ФИО", "Почта", "Телефон")

        val filteredUsers = remember(users, searchQuery, searchType, showAll, showFriends, currentUser) {
            if (searchQuery.isEmpty()) {
                if (showFriends) {
                    users.filter { it in currentUser.friends }
                } else {
                    users
                }
            } else {
                users.filter { user ->
                    val matchesSearch = when (searchType) {
                        "ФИО" -> {
                            val fullName = "${user.surname} ${user.name} ${user.patronymic ?: ""}".trim()
                            fullName.contains(searchQuery, ignoreCase = true) ||
                                    user.surname.contains(searchQuery, ignoreCase = true) ||
                                    user.name.contains(searchQuery, ignoreCase = true) ||
                                    user.patronymic?.contains(searchQuery, ignoreCase = true) ?: false
                        }
                        "Почта" -> user.mail.contains(searchQuery, ignoreCase = true)
                        "Телефон" -> user.telephone.contains(searchQuery, ignoreCase = true)
                        else -> true
                    }

                    val matchesFilter = if (showFriends) {
                        user in currentUser.friends
                    } else {
                        true
                    }

                    matchesSearch && matchesFilter
                }
            }
        }

        val sortedUsers = remember(filteredUsers, sortState) {
            when (sortState) {
                UsersSortState.NAME_ASC -> filteredUsers.sortedBy { "${it.surname} ${it.name} ${it.patronymic ?: ""}" }
                UsersSortState.NAME_DESC -> filteredUsers.sortedByDescending { "${it.surname} ${it.name} ${it.patronymic ?: ""}" }
            }
        }

        val (sortIcon, sortContentDescription) = remember(sortState) {
            when (sortState) {
                UsersSortState.NAME_ASC -> Pair(
                    Icons.Default.SortByAlpha,
                    "Сортировка по алфавиту (А-Я)"
                )
                UsersSortState.NAME_DESC -> Pair(
                    Icons.Default.SortByAlpha,
                    "Сортировка по алфавиту (Я-А)"
                )

            }
        }

        val onSortClick = {
            sortState = when (sortState) {
                UsersSortState.NAME_ASC -> UsersSortState.NAME_DESC
                UsersSortState.NAME_DESC -> UsersSortState.NAME_ASC
            }
        }

        val addToHistory = { query: String ->
            if (query.isNotBlank() && query !in searchHistory) {
                searchHistory = (searchHistory + query).takeLast(10)
            }
        }

        val clearHistory = {
            searchHistory = emptyList()
        }

        // endregion

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
                        if (searchQuery.isNotBlank()) {
                            addToHistory(searchQuery)
                        }
                    },
                    searchOptions = searchOptions,
                    placeholder = when (searchType) {
                        "ФИО" -> "Поиск по ФИО..."
                        "Почта" -> "Поиск по почте..."
                        "Телефон" -> "Поиск по телефону..."
                        else -> "Поиск пользователей..."
                    },
                    onClearClick = { searchQuery = "" },
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
                    onClearHistory = clearHistory,
                    showHistory = true,
                    showHistoryOnFocus = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = showAll,
                        onClick = {
                            showAll = !showAll
                            if (showAll) showFriends = false
                        },
                        label = { Text("Все") },
                        trailingIcon = if (showAll) {
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
                        selected = showFriends,
                        onClick = {
                            showFriends = !showFriends
                            if (showFriends) showAll = false
                        },
                        label = { Text("Друзья") },
                        trailingIcon = if (showFriends) {
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Сортировка: ${
                            when (sortState) {
                                UsersSortState.NAME_ASC -> "по алфавиту (А-Я)"
                                UsersSortState.NAME_DESC -> "по алфавиту (Я-А)"
                            }
                        }",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (sortedUsers.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.PersonOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (searchQuery.isEmpty()) {
                                    if (showFriends) "Нет друзей" else "Нет пользователей"
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
                            if (searchQuery != "") {
                                Text(
                                    text = "Найдено пользователей: ${sortedUsers.size}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                        items(sortedUsers) { user ->
                            user.CardInList(
                                currentUser = currentUser,
                                modifier = Modifier
                                    .clickable {
                                        selectedUser = user
                                        navController.navigate(NavRoutes.UserDetail.route)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}