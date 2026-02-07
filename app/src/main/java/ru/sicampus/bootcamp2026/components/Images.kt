package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R

@Composable
fun ProfilePicture(imageVector: ImageVector) {
    Image(
        imageVector = imageVector,
        contentDescription = "Аватар профиля",
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
    )
}

@Composable
fun ChangePFP() {
    Image(
        painter = painterResource(id = R.drawable.camera_add_svgrepo_com),
        contentDescription = "Изменить аватар",
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background),
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .scale(0.8f)
    )
}

@Composable
@Preview
fun ChangePFP_Preview(modifier: Modifier = Modifier) {
    ChangePFP()
}