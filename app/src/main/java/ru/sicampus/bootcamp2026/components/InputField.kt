package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.PrimaryGray
import ru.sicampus.bootcamp2026.ui.theme.SecondaryGray

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    title: String,
    state: TextFieldState,
    placeholderText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    error: String? = null,
    isPassword: Boolean = false,
    iconId: Int? = null
) {
    val passwordVisible = remember { mutableStateOf(false) }
    val outputTransformation = if (isPassword && !passwordVisible.value) {
        OutputTransformation {
            replace(0, length, "•".repeat(length))
        }
    } else null

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
        TextField(
            state = state,
            outputTransformation = outputTransformation,
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
                .onFocusChanged { focusState ->
                    onFocusChanged?.invoke(focusState.isFocused)
                },
            placeholder = {
                placeholderText?.let { text ->
                    Text(text = text)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            enabled = enabled,

            lineLimits = androidx.compose.foundation.text.input.TextFieldLineLimits.SingleLine,
            isError = error != null,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            imageVector = if (passwordVisible.value)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = PrimaryGray
                        )
                    }
                } else {
                    iconId?.let {
                        Icon(
                            imageVector = ImageVector.vectorResource(iconId),
                            contentDescription = null,
                            tint = Black
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = SecondaryGray,
                focusedContainerColor = SecondaryGray,
                disabledContainerColor = SecondaryGray,
                disabledTextColor = Black,
                unfocusedPlaceholderColor = PrimaryGray,
                focusedPlaceholderColor = PrimaryGray,
                disabledPlaceholderColor = PrimaryGray,
                errorContainerColor = SecondaryGray,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(20.dp),
        )

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}
