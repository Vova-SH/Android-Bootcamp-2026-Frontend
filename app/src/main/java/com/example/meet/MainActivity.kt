package com.example.meet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.meet.ui.navigation.AppNavigation
import com.example.meet.ui.theme.MeetTheme
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeetApp()
        }
    }
}

@ExperimentalSerializationApi
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MeetApp() {
    MeetTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            AppNavigation(navController = navController)
        }
    }
}