package com.example.planify.core.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.planify.R
import com.example.planify.main.common.themes.Locals


@Composable
fun GenericDialog(
    title: String,
    isOkEnabled: Boolean = true,
    isCancelEnabled: Boolean = true,
    buttonOkText: String? = null,
    buttonCancelText: String? = null,
    onDismiss: () -> Unit = {},
    onOk: () -> Unit = {},
    onCancel: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surface, shape = Locals.shapes.smallShape)
                .padding(Locals.spacing.m)
                .testTag("generic_dialog"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Locals.spacing.m)
            ) {
                // Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Content
                    content()
                }

                // Action button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (isCancelEnabled) {
                        TextButton(
                            modifier = Modifier
                                .testTag("generic_dialog_cancel_button"),
                            onClick = onCancel
                        ) {
                            Text(
                                text = buttonCancelText ?: stringResource(R.string.CANCEL),
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.primary
                            )
                        }
                    }

                    if (isOkEnabled) {
                        TextButton(
                            modifier = Modifier
                                .testTag("generic_dialog_ok_button"),
                            onClick = onOk
                        ) {
                            Text(
                                text = buttonOkText ?: stringResource(R.string.OK),
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
