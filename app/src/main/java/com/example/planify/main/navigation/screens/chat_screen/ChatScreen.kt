package com.example.planify.main.navigation.screens.chat_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.navigation.screens.chat_screen.components.ChatSkeletons

@Composable
fun ChatScreen(
    scaffoldPadding: PaddingValues
) {
    ChatScreen(
        viewModel = hiltViewModel(),
        scaffoldPadding = scaffoldPadding
    )
}

@Composable
private fun ChatScreen(
    viewModel: ChatScreenViewModel,
    scaffoldPadding: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is ChatUIState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = scaffoldPadding.calculateTopPadding() + Locals.spacing.s,
                        start = Locals.spacing.m,
                        end = Locals.spacing.m
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Locals.spacing.s)
            ) {
                repeat(6) {
                    ChatSkeletons()
                }
            }
        }
    }

}