package ru.sicampus.bootcamp2026.android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.android.ui.theme.Black
import ru.sicampus.bootcamp2026.android.ui.theme.Grey
import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    textColor: Color = TextGrey,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    focusRequester: FocusRequester? = null,
    keyboardActionOnNext: (KeyboardActionScope.() -> Unit)? = null,
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit)? = null,
    enabled: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,

        label = {
            Text(
                text = label,
                fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
                color = textColor
            )
        },

        visualTransformation = visualTransformation,

        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),

        keyboardActions = KeyboardActions(
            onNext = keyboardActionOnNext,
            onDone = keyboardActionOnDone
        ),

        modifier = modifier
            .let { if (focusRequester != null) it.focusRequester(focusRequester) else it }
            .padding(start = 25.dp, end = 25.dp)
            .fillMaxWidth(),

        singleLine = true,

        colors = TextFieldDefaults.colors(
            focusedTextColor = Black,
            unfocusedTextColor = Black,
            focusedContainerColor = Grey,
            unfocusedContainerColor = Grey,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),

        shape = RoundedCornerShape(40.dp)
    )
}