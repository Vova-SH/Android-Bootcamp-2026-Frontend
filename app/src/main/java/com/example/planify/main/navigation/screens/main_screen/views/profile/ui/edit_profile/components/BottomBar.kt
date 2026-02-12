package com.example.planify.main.navigation.screens.main_screen.views.profile.ui.edit_profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.FloppyDisk
import com.adamglin.phosphoricons.regular.X
import com.example.planify.R
import com.example.planify.main.common.themes.Locals


@Composable
fun BottomBar(
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Box(
       modifier = Modifier
           .fillMaxWidth()
           .background(color = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    PaddingValues(
                        bottom = WindowInsets.systemBars.asPaddingValues().calculateTopPadding(),
                        start = Locals.spacing.m,
                        end = Locals.spacing.m
                    )
                )
                .fillMaxWidth()
                .background(color = Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Locals.spacing.s)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(Locals.dimens.editProfileBottomBarButtonHeight),
                shape = Locals.shapes.mediumShape,
//                border = BorderStroke(
//                    color = Locals.extras.border,
//                    width = 1.dp
//                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colors.surface,
                    contentColor = colors.onSurface
                ),
                elevation = ButtonDefaults.buttonElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = PhosphorIcons.Regular.X,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(Locals.spacing.s))

                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Button(
                onClick = onSave,
                modifier = Modifier
                    .weight(1f)
                    .height(Locals.dimens.editProfileBottomBarButtonHeight),
                shape = Locals.shapes.mediumShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(hoveredElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = PhosphorIcons.Regular.FloppyDisk,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(Locals.spacing.s))

                    Text(
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

            }
        }
    }
}