package ru.sicampus.bootcamp2026.presentation.screen.creating_invitation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.presentation.components.AppButton
import ru.sicampus.bootcamp2026.presentation.components.AppTextField
import ru.sicampus.bootcamp2026.presentation.components.AppTitle
import ru.sicampus.bootcamp2026.presentation.components.ButtonContent
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatingInvitationScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFinished: () -> Unit
    // TODO(viewModel)
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                title = {},
                navigationIcon = {
                    AppButton(
                        modifier = Modifier
                            .height(56.dp)
                            .width(56.dp),
                        contentPadding = PaddingValues(6.dp),
                        content = ButtonContent.Icon(
                            icon = painterResource(R.drawable.ic_arrow_back),
                            size = 41.dp
                        ),
                        cornerRadius = 10.dp,
                        onClick = onBackClick
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 50.dp)
                .padding(top = 50.dp)
        ) {
            AppTitle(
                modifier = Modifier,
                titleText = stringResource(R.string.creating_invitation_title)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppTextField(
                    labelText = stringResource(R.string.email_guest_placeholder)
                )
                AppButton(
                    content = ButtonContent.Text(stringResource(R.string.invite_button)),
                    onClick = onFinished
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCreatingInvitationScreen() {
    AndroidBootcamp2026FrontendTheme {
        CreatingInvitationScreen(
            onBackClick = {},
            onFinished = {}
        )
    }
}