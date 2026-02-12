package com.example.planify.main.navigation.screens.fixed_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.core.content.pm.ShortcutInfoCompat
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Warning
import com.example.planify.R
import com.example.planify.main.common.themes.Locals

@Composable
fun ErrorScreen(
    status: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(Locals.icons.large),
            imageVector = PhosphorIcons.Regular.Warning,
            contentDescription = null,
            tint = Locals.extras.mutedForeground.copy(
                alpha = 0.4f
            )
        )
        Text(
            text = stringResource(R.string.error),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Normal
            ),
            color = Locals.extras.mutedForeground.copy(
                alpha = 0.4f
            )
        )
        Spacer(modifier = Modifier.height(Locals.spacing.xxxs))
        Text(
            text = "Status: $status",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            color = Locals.extras.mutedForeground.copy(
                alpha = 0.4f
            )
        )
    }
}