package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.utils.customDashedBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBackClicked: () -> Unit) {
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Профиль", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.size(24.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {

            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Приветствуем, Иван!",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(32.dp))

            ProfileField(
                title = "ФИО",
                value = "Иванов Иван Сергеевич"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileField(
                title = "Телефон",
                value = "+7-xxx-xxx-xx-xx"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileField(
                title = "Почта",
                value = "email@gmail.com"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileField(
                title = "День рождения",
                value = "хх мес. хххх (? лет/года)"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileField(
                title = "Описание",
                value = "Менеджер и разработчик"
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { /* TODO: Logout */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFE5E5),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    "Выйти из аккаунта",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(8.dp),
                    color = Color.Black.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileField(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .customDashedBorder()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { /* TODO */ },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onBackClicked = {})
}