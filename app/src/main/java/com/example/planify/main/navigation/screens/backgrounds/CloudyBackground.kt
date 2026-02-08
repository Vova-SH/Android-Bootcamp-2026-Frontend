package com.example.planify.main.navigation.screens.backgrounds

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.max


private data class CloudSpec(
    val yFrac: Float,
    val xFrac: Float,
    val scale: Float,
    val alpha: Float,
    val speed: Float
)

@Composable
fun CloudyBackground(
    modifier: Modifier = Modifier,
    cloudColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    tintColor: Color = MaterialTheme.colorScheme.background,  //Color(0xFFEEF4FF),
    animate: Boolean = true
) {
    val clouds = remember {
        listOf(
            CloudSpec(yFrac = 0.18f, xFrac = 0.15f, scale = 1.10f, alpha = 0.28f, speed = 12f),
            CloudSpec(yFrac = 0.28f, xFrac = 0.75f, scale = 0.95f, alpha = 0.22f, speed = 9f),
            CloudSpec(yFrac = 0.58f, xFrac = 0.10f, scale = 1.25f, alpha = 0.18f, speed = 7f),
            CloudSpec(yFrac = 0.72f, xFrac = 0.82f, scale = 1.05f, alpha = 0.16f, speed = 6f),
            CloudSpec(yFrac = 0.88f, xFrac = 0.40f, scale = 1.40f, alpha = 0.14f, speed = 5f),
        )
    }

    val t = if (animate) {
        val tr = rememberInfiniteTransition(label = "clouds")
        tr.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 60000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "t"
        ).value
    } else 0f

    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(color = tintColor)

        val w = size.width
        val h = size.height

        clouds.forEach { c ->
            val baseCloudW = w * 0.42f * c.scale
            val baseCloudH = baseCloudW * 0.36f

            val y = h * c.yFrac

            val drift = if (animate) (t * c.speed * 600f) else 0f
            val startX = w * c.xFrac
            val xWrapped = ((startX + drift) % (w + baseCloudW)) - baseCloudW * 0.5f
            val x = max(-baseCloudW, xWrapped)

            drawCloud(
                center = Offset(x, y),
                cloudW = baseCloudW,
                cloudH = baseCloudH,
                color = cloudColor
            )
        }
    }
}

private fun DrawScope.drawCloud(
    center: Offset,
    cloudW: Float,
    cloudH: Float,
    color: Color
) {
    fun layer(scale: Float, aMul: Float) {
        val w = cloudW * scale
        val h = cloudH * scale
        val left = center.x - w / 2f
        val top = center.y - h / 2f

        drawRoundRect(
            color = color.copy(alpha = color.alpha * aMul),
            topLeft = Offset(left, top + h * 0.28f),
            size = Size(w, h * 0.62f),
            cornerRadius = CornerRadius(h, h)
        )

        val r1 = h * 0.38f
        val r2 = h * 0.48f
        val r3 = h * 0.34f

        drawCircle(color = color.copy(alpha = color.alpha * aMul), radius = r1, center = Offset(left + w * 0.30f, top + h * 0.52f))
        drawCircle(color = color.copy(alpha = color.alpha * aMul), radius = r2, center = Offset(left + w * 0.50f, top + h * 0.42f))
        drawCircle(color = color.copy(alpha = color.alpha * aMul), radius = r3, center = Offset(left + w * 0.70f, top + h * 0.54f))
    }

    layer(scale = 1.10f, aMul = 0.65f)
    layer(scale = 1.00f, aMul = 1.00f)
}