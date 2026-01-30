package ru.sicampus.bootcamp2026.ui.auth

import androidx.compose.ui.text.style.TextIndent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.components.AuthTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ru.sicampus.bootcamp2026.R

@Composable
fun RegisterScreen(

    state: RegisterUIState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {


    Box(
        modifier = Modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.green_gradient),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop


        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .systemBarsPadding(), // чтобы не заезжало под статус бар
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.weight(4f))

            Text(
                text = "Create\naccount",
                fontSize = 85.sp,
                lineHeight = 88.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = MplusFontFamily

            )

            Spacer(modifier = Modifier.height(16.dp))

            // Карточка с полями
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(48.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2A2A2A).copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AuthTextField(
                        value = state.email,
                        onValueChange = onEmailChange,
                        label = "Email*",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    AuthTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        label = "Password*",
                        visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            IconButton(onClick = onTogglePasswordVisibility) {
                                Icon(
                                    imageVector = if (state.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Show password",
                                    tint = Color.Black.copy(alpha = 0.6f)
                                )
                            }
                        }
                    )

                    AuthTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        label = "Confirm password*",
                        visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            IconButton(onClick = onTogglePasswordVisibility) {
                                Icon(
                                    imageVector = if (state.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Show password",
                                    tint = Color.Black.copy(alpha = 0.6f)
                                )
                            }
                        }
                    )


                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Sign up",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MplusFontFamily)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
fun RegisterPreview() {
    RegisterScreen(
        state = RegisterUIState(),
        onEmailChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onTogglePasswordVisibility = {},
        onToggleConfirmPasswordVisibility = {},
        onLoginClick = {},
        onRegisterClick = {}
    )
}
