package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.ui.Modifier









import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold


import ru.sicampus.bootcamp2026.ui.theme.AppTheme
import ru.sicampus.bootcamp2026.ui.theme.nav.NavigationGraph
import ru.sicampus.bootcamp2026.ui.theme.screens.AuthScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    ) {
                        NavigationGraph(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
