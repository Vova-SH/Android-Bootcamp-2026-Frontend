package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.AppTheme
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkBlue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.Green
import ru.sicampus.bootcamp2026.ui.theme.LiteDarkBlue
import ru.sicampus.bootcamp2026.ui.theme.LiteGray
import ru.sicampus.bootcamp2026.ui.theme.Red
import ru.sicampus.bootcamp2026.ui.theme.White
import java.time.Instant
import java.time.ZoneId


enum class EventUiType { Offline, Hybrid, Online }

private fun EventUiType.dotColor(): Color = when (this) {
    EventUiType.Offline -> Blue
    EventUiType.Hybrid  -> Green
    EventUiType.Online  -> Red
}

@Composable
fun EventTypeSelector(
    value: EventUiType?,
    onValueChange: (EventUiType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val items = listOf(
            EventUiType.Offline to "Оффлайн",
            EventUiType.Hybrid to "Гибрид",
            EventUiType.Online to "Онлайн"
        )

        items.forEach { (type, label) ->
            val selected = type == value
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .background(
                        color = if (selected) DarkBlue  else Gray,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clickable { onValueChange(type) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(type.dotColor(), CircleShape)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = White
                    )
                }
            }
        }
    }
}


@Composable
fun EventDateField(
    value: String,
    onValueChange: (String) -> Unit,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Дата"
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = LiteDarkBlue
            )
        },
        trailingIcon = {
            IconButton(onClick = onIconClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "Выбрать дату",
                    tint = LiteDarkBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = White,
            unfocusedTextColor = White,
            disabledTextColor = LiteGray,
            focusedContainerColor = DarkBlue,
            unfocusedContainerColor = DarkBlue,
            disabledContainerColor = DarkBlue,
            focusedIndicatorColor = Blue,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = White
        )
    )
}


@Composable
fun EventTimeRow(
    startTime: String,
    endTime: String,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimeChip(
            label = startTime.ifBlank { "Время, от" },
            isEmpty = startTime.isBlank(),
            onClick = onStartClick,
            modifier = Modifier.weight(1f)
        )

        TimeChip(
            label = endTime.ifBlank { "Время, до" },
            isEmpty = endTime.isBlank(),
            onClick = onEndClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TimeChip(
    label: String,
    isEmpty: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .background(DarkBlue, RoundedCornerShape(28.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isEmpty) LiteDarkBlue else White
        )
    }
}


@Composable
fun EventTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(if (maxLines == 1) 56.dp else 56.dp),
        singleLine = maxLines == 1,
        maxLines = maxLines,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = LiteDarkBlue
            )
        },
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = White,
            unfocusedTextColor = White,
            disabledTextColor = LiteGray,
            focusedContainerColor = DarkBlue,
            unfocusedContainerColor = DarkBlue,
            disabledContainerColor = DarkBlue.copy(alpha = 0.3f),
            focusedIndicatorColor = Blue,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = White
        )

    )
}


@Composable
fun EventDescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = "Описание...",
                style = MaterialTheme.typography.bodyMedium,
                color = LiteDarkBlue
            )
        },
        singleLine = false,
        maxLines = 6,
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = White,
            unfocusedTextColor = White,
            disabledTextColor = LiteGray,
            focusedContainerColor = DarkBlue,
            unfocusedContainerColor = DarkBlue,
            disabledContainerColor = DarkBlue,
            focusedIndicatorColor = Blue,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = White
        )
    )
}

private val hours = (9..21).map { String.format("%02d:00", it) }


@Composable
fun TimePickerDialog(
    title: String,
    hours: List<String>,
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                hours.forEach { h ->
                    Text(
                        text = h,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onTimeSelected(h)
                                onDismiss()
                            }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
    modifier: Modifier = Modifier,
    onSuggestionClick: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        EventTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = it.isNotBlank()
            },
            placeholder = "Пригласить",
            modifier = Modifier.menuAnchor()
        )

        val filtered = suggestions.filter {
            value.isNotBlank() && it.contains(value, ignoreCase = true)
        }

        ExposedDropdownMenu(
            expanded = expanded && filtered.isNotEmpty(),
            onDismissRequest = { expanded = false }
        ) {
            filtered.forEach { email ->
                DropdownMenuItem(
                    text = { Text(email) },
                    onClick = {
                        onSuggestionClick(email)
                        expanded = false
                    }
                )
            }
        }
    }
}