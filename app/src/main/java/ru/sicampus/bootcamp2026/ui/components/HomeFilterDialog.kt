package ru.sicampus.bootcamp2026.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun HomeFilterDialog(
    onDismiss: () -> Unit,
    onApply: () -> Unit
){
    Dialog(onDismissRequest = onDismiss) {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .height(500.dp), // Высота окна
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF2A2A2A).copy(alpha = 0.6f)
        ){
            //TODO: Содержимое диалога фильтрации
        }
    }
}