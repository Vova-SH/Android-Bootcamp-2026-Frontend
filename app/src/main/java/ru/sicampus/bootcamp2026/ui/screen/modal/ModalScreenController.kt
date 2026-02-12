package ru.sicampus.bootcamp2026.ui.screen.modal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ModalScreenController {
    var isVisible by mutableStateOf(false)
        private set

    fun show() {
        isVisible = true
    }
    fun hide() {
        isVisible = false
    }
}

@Composable
fun rememberModalScreenController(): ModalScreenController {
    return ModalScreenController()
}