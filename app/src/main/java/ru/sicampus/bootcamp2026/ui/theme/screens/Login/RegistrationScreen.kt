package ru.sicampus.bootcamp2026.ui.theme.screens.Login

import ru.sicampus.bootcamp2026.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.DeepBlue
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

@Composable
fun RegistrationScreen(
    onRegistrationClick: () -> Unit,
    onIntent: (AuthIntent) -> Unit,

){
    var fullName by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.login_wave4),
            contentDescription = "Wave_4",
            modifier = Modifier
                .width(524.dp)
                .height(221.dp)
                .align(Alignment.TopStart)
                .offset(x = 0.dp, y = (-50).dp)
        )

        Image(
            painter = painterResource(R.drawable.login_wave3),
            contentDescription = "Wave_3",
            modifier = Modifier
                .width(524.dp)
                .height(221.dp)
                .align(Alignment.TopStart)
                .offset(x = (20).dp, y = (-50).dp)
        )
        Image(
            painter = painterResource(R.drawable.login_wave2),
            contentDescription = "Wave_2",
            modifier = Modifier
                .width(596.55.dp)
                .height(477.68.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-10).dp, y = 60.dp)
        )

        Image(
            painter = painterResource(R.drawable.login_wave1),
            contentDescription = "Wave_1",
            modifier = Modifier
                .width(596.55.dp)
                .height(477.68.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = 50.dp)
        )

        Surface(
            modifier = Modifier
                .width(368.dp)
                .height(625.dp)
                .align(Alignment.Center)
                .padding(6.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(34.dp))
                Text("Регистрация", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(39.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    value = fullName,
                    shape = RoundedCornerShape(6.dp),
                    onValueChange = { fullName = it },
                    label = {
                        Text(
                            "ФИО",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))



                var showJobDropdown by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { showJobDropdown = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            if (jobTitle.isNotEmpty()) jobTitle else "Должность",
                            fontSize = 14.sp
                        )
                    }

                    DropdownMenu(
                        expanded = showJobDropdown,
                        onDismissRequest = { showJobDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        jobTitle.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.toString()) },
                                onClick = {
                                    jobTitle = option.toString()
                                    showJobDropdown = false
                                }
                            )
                        }
                    }
                }

                var showDeptDropdown by remember { mutableStateOf(false) }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { showDeptDropdown = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            if (department.isNotEmpty()) department else "Отдел",
                            fontSize = 14.sp
                        )
                    }

                    DropdownMenu(
                        expanded = showDeptDropdown,
                        onDismissRequest = { showDeptDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        department.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.toString()) },
                                onClick = {
                                    department = option.toString()
                                    showDeptDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))


                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    value = email,
                    shape = RoundedCornerShape(6.dp),
                    onValueChange = { email = it },
                    label = {
                        Text(
                            "Email",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    value = password,
                    onValueChange = { password = it },
                    shape = RoundedCornerShape(6.dp),
                    label = {
                        Text(
                            "Пароль",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    value = passwordConfirm,
                    onValueChange = { passwordConfirm = it },
                    shape = RoundedCornerShape(6.dp),
                    label = {
                        Text(
                            "Повторите пароль",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onIntent(AuthIntent.Reg(fullName, jobTitle, email, password, passwordConfirm, department)) },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Зарегистрироваться")
                }

                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationScreenPreviewTablet() {
    AndroidBootcamp2026FrontendTheme {
        RegistrationScreen(
            onRegistrationClick = {},
            onIntent = { _ -> }
        )
    }
}


