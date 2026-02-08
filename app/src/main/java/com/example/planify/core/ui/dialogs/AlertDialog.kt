package com.example.planify.core.ui.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import com.example.planify.R


@Composable
fun AlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {
    GenericDialog(
        title = title,
        onDismiss = onDismiss,
        onCancel = onCancel,
        onOk = onDismiss,
        buttonCancelText = stringResource(R.string.CANCEL),
        buttonOkText = stringResource(R.string.OK)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
