package ru.sicampus.bootcamp2026.presentation.screen.my_invitations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.presentation.components.AppButton
import ru.sicampus.bootcamp2026.presentation.components.AppTitle
import ru.sicampus.bootcamp2026.presentation.components.ButtonContent
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInvitationsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
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
                titleText = stringResource(R.string.my_invitations_title)
            )

            Spacer(modifier = Modifier.height(50.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    InvitationItem(
                        onAccept = {},
                        onReject = {}
                    )
                }
                item {
                    InvitationItem(
                        onAccept = {},
                        onReject = {}
                    )
                }
            }
        }
    }
}

@Composable
fun InvitationItem(
    onAccept: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RectangleShape
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Текст 1",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Текст 2",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Текст 3",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Текст 4",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Текст 5",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                AppButton(
                    modifier = Modifier
                        .weight(1f),
                    content = ButtonContent.Text(
                        text = stringResource(R.string.accept_button),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W400
                        )
                    ),
                    cornerRadius = 10.dp,
                    contentPadding = PaddingValues(horizontal = 5.dp),
                    onClick = onAccept
                )

                AppButton(
                    modifier = Modifier
                        .weight(1f),
                    content = ButtonContent.Text(
                        text = stringResource(R.string.reject_button),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W400
                        )
                    ),
                    cornerRadius = 10.dp,
                    contentPadding = PaddingValues(horizontal = 5.dp),
                    onClick = onReject
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewMyInvitationsScreen() {
    AndroidBootcamp2026FrontendTheme {
        MyInvitationsScreen(
            onBackClick = {}
        )
    }
}