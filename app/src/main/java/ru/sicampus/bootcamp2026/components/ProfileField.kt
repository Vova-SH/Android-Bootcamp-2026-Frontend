package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkGrey

@Composable
fun ProfileField(
    label: String,
    value: String,
    isEditable: Boolean
    ) {
        var text by remember {mutableStateOf(value)}

        Text(label, fontSize = 14.sp, fontFamily = FontFamily(androidx.
        compose.ui.text.font.Font(R.font.montserrat_regular)), color = DarkGrey, modifier = Modifier)
        OutlinedTextField(
            value = text,
            onValueChange = {text = it},
            label = {label},
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Black ,
                unfocusedTextColor = Black,
                disabledTextColor = Black,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = if (isEditable) Blue else Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Black
            )
        )
    }