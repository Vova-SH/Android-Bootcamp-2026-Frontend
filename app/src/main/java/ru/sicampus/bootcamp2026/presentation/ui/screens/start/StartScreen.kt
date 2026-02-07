package ru.sicampus.bootcamp2026.presentation.ui.screens.start

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.presentation.ui.navigation.routes.AuthRoute
import ru.sicampus.bootcamp2026.presentation.ui.navigation.routes.RegRoute
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun StartScreen(
    nav: NavHostController
) {
    StartContent(
        onLoginClick = { nav.navigate(AuthRoute) },
        onRegisterClick = { nav.navigate(RegRoute) }
    )
}

@Composable
private fun StartContent(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Планируйте\nкорпоративные встречи",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 36.sp
            )
        }

        Column(
            modifier = Modifier.padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Кнопка Зарегистрироваться
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = "Зарегистрироваться",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Кнопка Войти
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Войти",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun StartScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        StartContent(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF111318,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun StartScreenDarkPreview() {
    AndroidBootcamp2026FrontendTheme {
        StartContent(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}