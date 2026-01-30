package ru.sicampus.bootcamp2026

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.Dimension
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import androidx.compose.runtime.*

@Composable
fun RegistrationScreen() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (card, avatarCircle, title, subtitle, nameField, surnameField, emailField, passwordField, confirmPasswordField, button) = createRefs()

        var name by remember { mutableStateOf("Иван") }
        var surname by remember { mutableStateOf("Иванов") }
        var email by remember { mutableStateOf("example@example.com") }
        var password by remember { mutableStateOf("123456789") }
        var confirmPassword by remember { mutableStateOf("123456789") }

        Card(
            modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(parent.top, 83.dp)
                    start.linkTo(parent.start, 28.dp)
                    end.linkTo(parent.end, 28.dp)
                    bottom.linkTo(parent.bottom, 28.dp) // Added bottom constraint
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints // Changed to fillToConstraints
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
        }


        Surface(
            modifier = Modifier
                .constrainAs(avatarCircle) {
                    top.linkTo(parent.top, 100.dp)
                    start.linkTo(parent.start, 143.dp)
                }
                .size(118.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {

        }

        // Заголовок "Регистрация"
        Text(
            text = "Регистрация",
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, 273.dp)
                start.linkTo(parent.start, 140.dp)
            },
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Подзаголовок "Создайте новый аккаунт"
        Text(
            text = "Создайте новый аккаунт",
            modifier = Modifier.constrainAs(subtitle) {
                top.linkTo(parent.top, 316.dp)
                start.linkTo(parent.start, 84.dp)
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        // Поле "Имя"
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя", style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .constrainAs(nameField) {
                    top.linkTo(parent.top, 409.dp)
                    start.linkTo(parent.start, 65.dp)
                }
                .width(121.dp)
                .height(62.dp),
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // Поле "Фамилия"
        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Фамилия", style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .constrainAs(surnameField) {
                    top.linkTo(parent.top, 409.dp)
                    start.linkTo(parent.start, 218.dp)
                }
                .width(121.dp)
                .height(62.dp),
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // Поле "Email"
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .constrainAs(emailField) {
                    top.linkTo(parent.top, 509.dp)
                    start.linkTo(parent.start, 65.dp)
                }
                .width(274.dp)
                .height(62.dp),
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // Поле "Пароль"
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль", style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .constrainAs(passwordField) {
                    top.linkTo(parent.top, 609.dp)
                    start.linkTo(parent.start, 65.dp)
                }
                .width(274.dp)
                .height(62.dp),
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation() // Скрыть пароль
        )

        // Поле "Подтверждение пароля"
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Подтверждение пароля", style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .constrainAs(confirmPasswordField) {
                    top.linkTo(parent.top, 709.dp)
                    start.linkTo(parent.start, 65.dp)
                }
                .width(274.dp)
                .height(62.dp),
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation() // Скрыть пароль
        )

        // Кнопка "Зарегистрироваться"
        Button(
            onClick = { /* TODO: Логика регистрации */ },
            modifier = Modifier
                .constrainAs(button) {
                    top.linkTo(parent.top, 810.dp)
                    start.linkTo(parent.start, 146.dp)
                }
                .width(207.dp)
                .height(56.dp),
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Зарегистрироваться",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        Box(Modifier.size(360.dp, 640.dp)) {
            RegistrationScreen()
        }
    }
}