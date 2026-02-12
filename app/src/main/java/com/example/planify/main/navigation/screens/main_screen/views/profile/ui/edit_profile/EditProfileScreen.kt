package com.example.planify.main.navigation.screens.main_screen.views.profile.ui.edit_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.planify.main.features.profiles.data.repositories_impl.EditProfileUIState
import com.example.planify.main.navigation.screens.fixed_screens.ErrorScreen

@Composable
fun EditProfileScreen(
    onCameraClick: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onCancel: () -> Unit
) {
    EditProfileScreen(
        viewModel = hiltViewModel(),
        onCameraClick = onCameraClick,
        onSave = onSave,
        onBack = onBack,
        onCancel = onCancel
    )
}

@Composable
private fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    onCameraClick: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onCancel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is EditProfileUIState.Saved) viewModel.save()
    }

    when(uiState) {
        is EditProfileUIState.Loading -> { } // TODO: profile edit skeletons
        is EditProfileUIState.Error -> { ErrorScreen((uiState as EditProfileUIState.Error).message) }
        else -> {
            EditScreenUI(
                viewModel = viewModel,
                onCameraClick = onCameraClick,
                onBack = onBack,
                onSave = onSave,
                onCancel = onCancel
            )
        }
    }


}

