package com.example.planify.main.navigation.screens.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.MagnifyingGlass
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.PlaceholderText

@Composable
fun GlassSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String
) {
    val colors = MaterialTheme.colorScheme
    val extras = Locals.extras
    val shape = Locals.shapes.mediumShape

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Locals.dimens.searchBarHeight)
            .shadow(
                elevation = Locals.dimens.elevation,
                shape = shape,
                clip = false,
                spotColor = extras.glass.shadow
            )
            .clip(shape)
            .background(extras.glass.bgStrong)
            .padding(horizontal = Locals.spacing.m),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = PhosphorIcons.Regular.MagnifyingGlass,
                contentDescription = null,
                tint = colors.onPrimaryContainer.copy(alpha = 0.65f),
                modifier = Modifier
                    .size(Locals.icons.smallPlus),
            )

            Spacer(modifier = Modifier.width(Locals.spacing.s))

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {}),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 18.sp,
                        color = colors.onPrimaryContainer
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier,
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                PlaceholderText(
                                    modifier = Modifier,
                                    text = placeholder
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}
