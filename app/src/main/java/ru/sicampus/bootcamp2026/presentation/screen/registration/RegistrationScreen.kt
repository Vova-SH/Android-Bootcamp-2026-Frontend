package ru.sicampus.bootcamp2026.presentation.screen.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.presentation.components.AppButton
import ru.sicampus.bootcamp2026.presentation.components.ButtonContent
import ru.sicampus.bootcamp2026.presentation.components.AppTextField
import ru.sicampus.bootcamp2026.presentation.components.AppTitle
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onNavigateToAuthorization: () -> Unit,
    onFinished: () -> Unit
    // TODO(viewModel)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 50.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AppTitle(
            modifier = Modifier,
            titleText = stringResource(R.string.sign_up_title)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTextField(
                labelText = stringResource(R.string.name_placeholder)
            )
            AppTextField(
                labelText = stringResource(R.string.surname_placeholder)
            )
            AppTextField(
                labelText = stringResource(R.string.patronymic_placeholder)
            )
            AppTextField(
                labelText = stringResource(R.string.email_placeholder)
            )
            AppTextField(
                labelText = stringResource(R.string.password_placeholder)
            )
            AppTextField(
                labelText = stringResource(R.string.password_placeholder)
            )
            AppButton(
                content = ButtonContent.Text(stringResource(R.string.sign_up_button)),
                onClick = onFinished
            )
            Text(
                modifier = Modifier.clickable {
                    onNavigateToAuthorization()
                },
                text = stringResource(R.string.already_have_account),
                fontWeight = FontWeight.W400,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
fun PreviewRegistration() {
    AndroidBootcamp2026FrontendTheme {
        RegistrationScreen(
            onNavigateToAuthorization = {},
            onFinished = {}
        )
    }
}