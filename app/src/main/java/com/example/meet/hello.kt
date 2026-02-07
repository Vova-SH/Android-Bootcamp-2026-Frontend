package com.example.meet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HelloScreen(){
    Text(
        text = "Тут мой текст",
        color = Color.White
    )
}

@Preview
@Composable
fun Show(){
    HelloScreen()
}