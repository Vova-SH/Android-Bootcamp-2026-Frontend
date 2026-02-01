package ru.sicampus.bootcamp2026.components;

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchType: String,
    onSearchTypeChange: (String) -> Unit,
    searchOptions: List<String>,
    placeholder: String = "Поиск...",
    modifier: Modifier = Modifier,
    onClearClick: () -> Unit = {},
    onSearchAction: () -> Unit = {},
    onSortClick: (() -> Unit)? = null,
    sortIcon: ImageVector? = null,
    sortContentDescription: String? = null,
    searchHistory: List<String> = emptyList(),
    onHistoryItemClick: (String) -> Unit = {},
    onAddToHistory: (String) -> Unit = {},
    onClearHistory: () -> Unit = {}, // Новый параметр для очистки истории
    showHistory: Boolean = true,
    showHistoryOnFocus: Boolean = true
) {
    var expandedSearchType by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var showHistoryDropdown by remember { mutableStateOf(false) }
    var showClearHistoryDialog by remember { mutableStateOf(false) } // Диалог подтверждения
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    // Отфильтрованная история для автодополнения
    val filteredHistory = remember(searchQuery, searchHistory) {
        if (searchQuery.isEmpty()) {
            searchHistory.take(5)
        } else {
            searchHistory.filter { it.contains(searchQuery, ignoreCase = true) }.take(5)
        }
    }

    // Показывать историю при фокусе или при вводе текста
    val shouldShowHistory = remember(isFocused, searchQuery, filteredHistory) {
        showHistory && filteredHistory.isNotEmpty() && (isFocused || searchQuery.isNotEmpty())
    }

    LaunchedEffect(isFocused) {
        if (isFocused && showHistoryOnFocus && searchHistory.isNotEmpty()) {
            showHistoryDropdown = true
        } else if (!isFocused) {
            showHistoryDropdown = false
        }
    }

    // Диалог подтверждения очистки истории
    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = { Text("Очистить историю поиска?") },
            text = { Text("Все сохранённые поисковые запросы будут удалены. Это действие нельзя отменить.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearHistory()
                        showClearHistoryDialog = false
                        showHistoryDropdown = false
                    }
                ) {
                    Text("Очистить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearHistoryDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    Column(modifier = modifier) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { text ->
                            onSearchQueryChange(text)
                            if (text.isNotEmpty()) {
                                showHistoryDropdown = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                                if (focusState.isFocused && showHistoryOnFocus && searchHistory.isNotEmpty()) {
                                    showHistoryDropdown = true
                                }
                            },
                        textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onSurface),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                onSearchAction()
                                showHistoryDropdown = false
                                if (searchQuery.isNotBlank()) {
                                    onAddToHistory(searchQuery)
                                }
                            }
                        ),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                // Кнопка истории поиска
                if (searchHistory.isNotEmpty() && showHistory) {
                    Box {
                        IconButton(
                            onClick = {
                                if (showHistoryDropdown) {
                                    showHistoryDropdown = false
                                } else {
                                    showHistoryDropdown = true
                                    focusRequester.requestFocus()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "История поиска",
                                tint = if (showHistoryDropdown)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Кнопка очистки текущего поиска
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onClearClick()
                            focusManager.clearFocus()
                            showHistoryDropdown = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Очистить поиск",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Кнопка сортировки
                if (onSortClick != null && sortIcon != null) {
                    IconButton(
                        onClick = {
                            onSortClick()
                            showHistoryDropdown = false
                        }
                    ) {
                        Icon(
                            imageVector = sortIcon,
                            contentDescription = sortContentDescription,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Кнопка выбора типа поиска
                if (searchOptions.isNotEmpty()) {
                    Box {
                        IconButton(
                            onClick = {
                                expandedSearchType = true
                                showHistoryDropdown = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Тип поиска",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = expandedSearchType,
                            onDismissRequest = { expandedSearchType = false }
                        ) {
                            searchOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            option,
                                            color = if (option == searchType)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        onSearchTypeChange(option)
                                        expandedSearchType = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Выпадающий список истории поиска
        if (showHistoryDropdown && shouldShowHistory) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    // Заголовок истории
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "История поиска",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${searchHistory.size}/10",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Разделитель
                    Divider(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        thickness = 1.dp
                    )

                    // Элементы истории
                    filteredHistory.forEachIndexed { index, historyItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onHistoryItemClick(historyItem)
                                    showHistoryDropdown = false
                                    if (historyItem.isNotBlank()) {
                                        onAddToHistory(historyItem)
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = historyItem,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "В ${searchType.lowercase()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Разделитель между элементами (кроме последнего)
                        if (index < filteredHistory.size - 1) {
                            Divider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                thickness = 0.5.dp
                            )
                        }
                    }

                    // Кнопка очистки истории
                    Divider(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        thickness = 1.dp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showClearHistoryDialog = true
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ClearAll,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Очистить историю",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}