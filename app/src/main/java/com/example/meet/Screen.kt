package com.example.meet

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
}