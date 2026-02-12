package com.example.planify.main.common.ui

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.illegalDecoyCallException
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.BeerStein
import com.example.planify.R
import com.example.planify.main.common.themes.Locals
import kotlinx.serialization.Contextual


@Composable
fun TextEmptyMeetings() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Locals.spacing.s),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_meetings_today),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Normal
            ),
            color = Locals.extras.mutedForeground.copy(alpha = 0.75f),
        )
        Icon(
            modifier = Modifier
                .size(Locals.icons.mediumPlus),
            imageVector = PhosphorIcons.Regular.BeerStein,
            contentDescription = null,
            tint = Locals.extras.mutedForeground.copy(alpha = 0.75f)
        )
    }
}
@Composable
fun TextOnSurface(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 12.sp
    )
}

@Composable
fun TextOnSurface(text: String, modifier: Modifier = Modifier, selected: Boolean) {
    Text(
        modifier = modifier,
        text = text,
        style = if (selected) MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold
        ) else MaterialTheme.typography.labelMedium,
        color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun PlaceholderText(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Normal
        ),
        color = Locals.extras.mutedForeground
    )
}

@Composable
fun TopBarTitleText(title: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.displayLarge.copy(
            brush = Locals.gradients.blue
        ),
    )
}

@Composable
fun TopBarTitleTextLarge(title: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.displayLarge.copy(
            brush = Locals.gradients.blue,
            fontSize = 40.sp
        ),
    )
}

@Composable
fun TopBarTitleTextSecondary(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
        fontSize = 14.sp
    )
}

@Composable
fun TopNavBarItemText(text: String, isSelected: Boolean,  modifier: Modifier = Modifier) {
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = contentColor
    )
}

@Composable
fun UpcomingEventText(
    time: String,
    event: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = event,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}