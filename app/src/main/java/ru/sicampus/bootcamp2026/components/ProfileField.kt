package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileField(
    label: String,
    value: String,
    isEditable: Boolean = false,
    isContact: Boolean = false,
    modifier: Modifier = Modifier,
    onClick : () -> Unit = {
        // TODO делать чтобы копировалось в буфер обмен
    }
) {
    if (!isEditable) {
        Column(
            modifier
                .clickable(onClick = onClick)
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = label,
                color = Color.Gray,
                modifier = Modifier
                    .padding(bottom = 5.dp)
            )
            Text(
                text = value
            )
        }
    } else {
        var text by remember { mutableStateOf(value) }
        if (!isContact) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = {
                    Text(label)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            )
        } else {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = {
                    Text(label)
                },
                readOnly = true,
                trailingIcon = {
                    if (isContact) {
                        Icon(
                            Icons.Filled.Edit,
                            "",
                            Modifier
                                .clickable {
                                    // TODO
                                }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun ProfileFieldPreview() {
    ProfileField(
        label = "Почта",
        value = "fed-cat@bk.ru",
        isContact = true,
        isEditable = true
    )
}
