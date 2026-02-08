package com.example.planify.main.navigation.screens.notifications_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.example.planify.R
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.navigation.AppRoute
import com.example.planify.main.navigation.screens.main_screen.components.NotificationIcon

@Composable
fun NotificationScreenTopBar(
    height: Dp,
    navController: NavController
) {
    val colors = MaterialTheme.colorScheme

    Box(
       modifier = Modifier
           .background(color = colors.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = Locals.spacing.m,
                    end = Locals.spacing.m,
                    bottom = Locals.spacing.s
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.notifications),
                    style = MaterialTheme.typography.displayLarge.copy(
                        brush = Locals.gradients.blue
                    )
                )

                Spacer(modifier = Modifier.height(Locals.spacing.xs))

                Text(
                    text = stringResource(R.string.notifications),
                    style = MaterialTheme.typography.bodyMedium.copy(),
                    color = colors.onSurface
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            NotificationIcon(
                modifier = Modifier
                    .size(Locals.icons.medium),
                onClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}