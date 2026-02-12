package com.example.planify.main.navigation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.planify.R
import com.example.planify.core.ui.dialogs.AlertDialog

@Composable
fun AuthRequiredDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = stringResource(R.string.authentication_required),
        message = stringResource(R.string.authenticate_again),
        onDismiss = onDismiss,
        onCancel = { }
    )
}
