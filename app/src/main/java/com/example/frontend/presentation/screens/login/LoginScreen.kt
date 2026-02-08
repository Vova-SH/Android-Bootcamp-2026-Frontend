package com.example.frontend.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.data.auth.SessionManager
import com.example.frontend.presentation.theme.Purple
import com.example.frontend.presentation.viewmodel.MeetupViewModel
import com.example.frontend.presentation.viewmodel.AuthState

@Composable
fun LoginScreen(
    viewModel: MeetupViewModel,
    sm: SessionManager,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var l by remember { mutableStateOf("") }
    var p by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(false) }
    
    val aus by viewModel.authState.collectAsState()
    
    // переходит на главный экран после успеха
    LaunchedEffect(aus) {
        if (aus is AuthState.Success) {
            onLoginSuccess()
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Meetups",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Purple
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            OutlinedTextField(
                value = l,
                onValueChange = { l = it },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple,
                    focusedLabelColor = Purple
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val trans = if (show) VisualTransformation.None else PasswordVisualTransformation()
            val icon = if (show) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val desc = if (show) "Скрыть пароль" else "Показать пароль"
            
            OutlinedTextField(
                value = p,
                onValueChange = { p = it },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = trans,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { show = !show }) {
                        Icon(imageVector = icon, contentDescription = desc)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple,
                    focusedLabelColor = Purple
                )
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            val loading = aus is AuthState.Loading
            val ok = l.isNotBlank() && p.isNotBlank()
            
            Button(
                onClick = {
                    if (ok) {
                        sm.saveCredentials(l, p)
                        viewModel.login(l, p)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple),
                enabled = !loading && ok
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(text = "Войти", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            if (aus is AuthState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (aus as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            TextButton(onClick = onRegisterClick) {
                Text(text = "Нет аккаунта? Зарегистрироваться", color = Purple)
            }
        }
    }
}
