package com.example.planify.main.features.create_meet_dialog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.planify.R
import com.example.planify.main.common.themes.Locals

@Composable
fun CreateMeetDialogView(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
)  {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = Locals.shapes.mediumShape,
            tonalElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(Locals.spacing.l)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(Locals.spacing.s)
                    )
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.Start)
                            .padding(horizontal = Locals.spacing.s),
                        text = stringResource(R.string.new_meet),
                        fontSize = 36.sp
                    )
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.Start)
                            .padding(horizontal = Locals.spacing.s),
                        text = stringResource(R.string.create_new_meet),
                        fontSize = 16.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(Locals.spacing.s),
                            onClick = onConfirm,
                            shape = Locals.shapes.mediumShape
                        ) {
                            Text(stringResource(R.string.btn_create))
                        }
                        Button(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(Locals.spacing.s),
                            onClick = onDismiss,
                            shape = Locals.shapes.mediumShape
                        ) {
                            Text(stringResource(R.string.btn_cancel))
                        }
                    }
                }
            }
        }
    }
}