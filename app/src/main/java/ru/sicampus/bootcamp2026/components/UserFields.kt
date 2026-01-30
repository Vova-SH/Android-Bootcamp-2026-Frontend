package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.IconCompat
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.Black
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.DarkGrey
import ru.sicampus.bootcamp2026.ui.theme.LightGrey

@Composable
fun UserField(
    label: String,
    hint: String,
    value: String,
    isEditable: Boolean,
    iconId: Int,
) {
    var text by remember {mutableStateOf(value)}

    Text(label, fontSize = 14.sp, fontFamily = FontFamily(androidx.
    compose.ui.text.font.Font(R.font.montserrat_regular)), color = Blue, modifier = Modifier
        .padding(start=24.dp))
    OutlinedTextField(
        value = text,
        onValueChange = {text = it},
        label = {},
        placeholder = {
            Text(hint, fontSize = 14.sp, fontFamily = FontFamily(androidx.
            compose.ui.text.font.Font(R.font.montserrat_regular)), color = DarkGrey)
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(63.dp)
            .padding(horizontal = 24.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Black ,
            unfocusedTextColor = Black,
            disabledTextColor = Black,
            focusedContainerColor = LightGrey,
            unfocusedContainerColor = LightGrey,
            focusedIndicatorColor = if (isEditable) Blue else Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Black
        ),
        leadingIcon = { Icon(painter = painterResource(id = iconId), contentDescription = "Иконка",
            tint = DarkGrey,
            modifier = Modifier.padding(end=5.dp))},
    )
}
