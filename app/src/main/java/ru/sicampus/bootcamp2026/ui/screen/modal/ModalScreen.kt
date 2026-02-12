package ru.sicampus.bootcamp2026.ui.screen.modal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.ui.theme.Black

@Composable
fun ModalScreen(
    controller: ModalScreenController,
    content: @Composable (hide: suspend () -> Unit) -> Unit
) {
    if (!controller.isVisible) return

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topOffset = screenHeight * 0.06f
    val modalHeight = screenHeight - topOffset

    val offsetY = remember {
        Animatable(modalHeight.value)
    }
    val scrimAlpha = remember {
        Animatable(0f)
    }
    val duration = 800

    val hide: suspend  () -> Unit = {
        coroutineScope {
            launch {
                offsetY.animateTo(
                    targetValue = modalHeight.value,
                    animationSpec = tween(
                        durationMillis = duration
                    )
                )
            }
            launch {
                scrimAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween (duration)
                )
            }
        }

        controller.hide()
    }
    LaunchedEffect(Unit) {
        launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = duration
                )
            )
        }
        launch {
            scrimAlpha.animateTo(
                targetValue = 0.8f,
                animationSpec = tween (duration)
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().zIndex(1f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black.copy(alpha = scrimAlpha.value))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(modalHeight)
                .align(Alignment.BottomCenter)
                .offset {
                    IntOffset(
                        x = 0,
                        y = offsetY.value.toInt()
                    )
                }
        ) {
            content(hide)
        }
    }
}