package ru.sicampus.bootcamp2026.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.components.ProfileField
import ru.sicampus.bootcamp2026.currentUser
import ru.sicampus.bootcamp2026.data.UserData
import ru.sicampus.bootcamp2026.selectedUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    isCurrentUser: Boolean = false,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val fraction = scrollBehavior.state.collapsedFraction

    var isEditable by remember { mutableStateOf(false) }
    (if (isCurrentUser) currentUser else selectedUser)?.let { user ->
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = user.surname + " " + user.name + " " + (user.patronymic ?: ""),
                            color = Color.White,
                            fontSize = lerp(24.sp, 20.sp, fraction)
                        )
                    },
                    navigationIcon = {
                        if (!isEditable) {
                            if (!isCurrentUser) {
                                IconButton({ navController.popBackStack() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        "Back",
                                        tint = Color.White
                                    )
                                }
                            }
                        } else {
                            IconButton({ isEditable = false }) {
                                Icon(Icons.Default.Close, "Отмена", tint = Color.White)
                            }
                        }
                    },
                    actions = {
                        Row {
                            if (!isEditable && isCurrentUser) {
                                IconButton({ isEditable = true }) {
                                    Icon(Icons.Filled.Edit, "Edit", tint = Color.White)
                                }
                                IconButton({ /* TODO */ }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ExitToApp,
                                        "Logout",
                                        tint = Color.White
                                    )
                                }
                            } else if (isEditable) {
                                IconButton({ /* TODO */ }) {
                                    Icon(Icons.Default.Check, "Сохранить", tint = Color.White)
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Blue,

                        ),
                    scrollBehavior = scrollBehavior
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxWidth()) {
                user.avatar?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .alpha(1f - fraction)
                    )
                }
                Column(
                    modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                ) {
                    if (isEditable) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button({

                            }) {
                                Text("Нажмите, чтобы сменить аватар")
                            }
                        }
                    }
                    if (isEditable) {
                        var fio by remember {
                            mutableStateOf(
                                (user.surname + " " + user.name + " " + user.patronymic) ?: ""
                            )
                        }
                        TextField(
                            value = fio,
                            onValueChange = { fio = it },
                            label = {
                                Text("ФИО")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileField("Телефон", user.telephone, isEditable)
                    ProfileField("Email", user.mail, isEditable)
                    Row(Modifier.fillMaxWidth(), Arrangement.Center) { Text("Контакты:") }
                    if (user.contacts.isEmpty()) {
                        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                            Text("Контакты не добавлены")
                        }
                    } else {
                    user.contacts.forEach { (label, values) ->
                        ProfileField(label, values, isEditable, true)
                    }
                        }
                    if (isEditable) {
                        Button(
                            {}
                        ) {
                            Text("Добавить контакт")
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Профиль"
)
@Composable
fun ShowProfile() {
    val user1 = UserData(
        surname = "Иванов",
        name = "Иван",
        patronymic = "Иванович",
        telephone = "+79111234567",
        mail = "ivanov@example.com"
    )

    val user2 = UserData(
        surname = "Петрова",
        name = "Мария",
        patronymic = "Сергеевна",
        telephone = "+79117654321",
        mail = "petrova@example.com"
    )

    val user3 = UserData(
        surname = "Сидоров",
        name = "Алексей",
        patronymic = null,
        telephone = "+79119876543",
        mail = "sidorov@example.com"
    )

    val user4 = UserData(
        surname = "Козлова",
        name = "Анна",
        patronymic = "Дмитриевна",
        telephone = "+79115556677",
        mail = "kozlova@example.com"
    )
    user1.friends.add(user2)
    user1.friends.add(user3)
    user1.friends.add(user4)

    user1.contacts["telegram"] = "@asfasff"
    user1.contacts["vk"] = "safafa"
    user1.contacts["gsggs"] = "sdasg"

    user1.avatar = ImageBitmap.imageResource(R.drawable.img_1);

//    MaterialTheme {
//        ProfileScreen(NavHostController(), user1, true)
//    }
}

