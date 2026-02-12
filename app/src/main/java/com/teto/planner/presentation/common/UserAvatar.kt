package com.teto.planner.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.teto.planner.domain.model.user.LoadStatus

@Composable
fun UserAvatar(
    url: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val model = remember(url) {
        if (!url.isNullOrBlank()) url else null
    }

    if (model != null) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(model)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    } else {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxSize(0.6f)
            )
        }
    }
}


fun getStatusColor(status: LoadStatus): Color {
    return when (status) {
        LoadStatus.LOW -> Color(0xFF4CAF50)
        LoadStatus.MEDIUM -> Color(0xFFFFC107)
        LoadStatus.HIGH -> Color(0xFFF44336)
    }
}