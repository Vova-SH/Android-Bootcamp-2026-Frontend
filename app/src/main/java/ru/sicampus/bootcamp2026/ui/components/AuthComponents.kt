package ru.sicampus.bootcamp2026.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

val InputBg = Color(0xFFC4C4C4).copy(alpha = 0.4f) //вынести в theme/Color.kt
    @Composable
    fun AuthTextField(
        value: String,
        onValueChange: (String) -> Unit, // пользователь ввел, обновляем переменную
        label: String,
        modifier: Modifier,
        trailingIcon: @Composable (() -> Unit)? = null,// для иконки в конце поля
        visualTransformation: VisualTransformation = VisualTransformation.None,// для добавление точек на пароль и тп (форматирование)
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default// настройка софтверной клавы, (онли цифры и тп)
    ){
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {Text(text = label, color = Color.Black.copy(alpha = 0.6f))},
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min =56.dp),
            shape = RoundedCornerShape(percent = 50),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBg,
                unfocusedContainerColor = InputBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            singleLine = true
        )
    }

@Preview(showBackground = true)
@Composable
fun AuthTextFieldPreview() {
    var text by remember { mutableStateOf("Email*") }
    AuthTextField(
        value = text,
        onValueChange = { text = it },
        label = "Email",
        modifier = Modifier
    )
}
