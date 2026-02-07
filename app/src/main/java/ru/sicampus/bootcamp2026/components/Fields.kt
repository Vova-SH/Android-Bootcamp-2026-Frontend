package ru.sicampus.bootcamp2026.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.border
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.width(300.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        shape = RoundedCornerShape(15.dp),
        label = {
            Text(
                text = label,
                fontSize = 16.sp
            )
        },
        placeholder = placeholder,
        singleLine = true,
        leadingIcon = leadingIcon,
        modifier = modifier,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            focusedBorderColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun EditableField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.width(300.dp),
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
) {
    var enabled by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(15.dp),
        label = {
            Text(
                text = label,
                fontSize = 16.sp
            )
        },
        placeholder = placeholder,
        singleLine = true,
        leadingIcon = leadingIcon,
        modifier = modifier,
        trailingIcon = {
            IconButton(
                onClick = {
                    enabled = !enabled
                }
            ) {
                Image(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
        },
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
            disabledLabelColor = MaterialTheme.colorScheme.onBackground,
        )
    )
}