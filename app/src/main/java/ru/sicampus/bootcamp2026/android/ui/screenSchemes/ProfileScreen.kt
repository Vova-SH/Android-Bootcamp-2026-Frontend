package ru.sicampus.bootcamp2026.android.ui.screenSchemes
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.modifier.modifierLocalConsumer
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import ru.sicampus.bootcamp2026.R
//import ru.sicampus.bootcamp2026.android.ui.components.CustomNavigationBar
//import ru.sicampus.bootcamp2026.android.ui.components.CustomTextField
//import ru.sicampus.bootcamp2026.android.ui.theme.AppTheme
//import ru.sicampus.bootcamp2026.android.ui.theme.Black
//import ru.sicampus.bootcamp2026.android.ui.theme.DarkBlue
//import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen (
//    onBackClick: () -> Unit = {},
//    onEditClick: () -> Unit = {},
//    onExitClick: () -> Unit = {},
//    onHomeClick: () -> Unit = {},
//    onProfileClick: () -> Unit = {}
//) {
//
//    var email by remember { mutableStateOf("") }
//    var fullName by remember { mutableStateOf("") }
//    var department by remember { mutableStateOf("") }
//    var position by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    Scaffold(
//        bottomBar = {
//            CustomNavigationBar(
//                2,
//                onHomeClick,
//                {},
//                onProfileClick
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.End,
//            ) {
//
//
//                Icon(
//                    imageVector = Icons.Default.Edit,
//                    contentDescription = "Изменить профиль",
//                    tint = Black,
//                    modifier = Modifier
//                        .size(30.dp)
//                )
//
//                Spacer(modifier = Modifier.width(30.dp))
//
//                Icon(
//                    painter = painterResource(R.drawable.close_icon),
//                    contentDescription = "Выйти",
//                    tint = Black,
//                    modifier = Modifier.size(30.dp)
//                )
//            }
//
//            Icon(
//                imageVector = Icons.Default.AccountCircle,
//                contentDescription = "Фото профиля",
//                modifier = Modifier.size(150.dp),
//                tint = DarkBlue
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "Зубенко Михаил Петрович",
//                fontSize = 16.sp,
//                fontFamily = FontFamily(Font(R.font.open_sans_extrabold)),
//                color = Black,
//                modifier = Modifier.padding(horizontal = 16.dp)
//            )
//
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(20.dp)
//            ) {
//
//                Column {
//                    Text(
//                        text = "Почта",
//                        fontSize = 14.sp,
//                        fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
//                        color = TextGrey,
//                        modifier = Modifier.padding(horizontal = 24.dp)
//
//                    )
//
//                    //  Почта
//                    CustomTextField(
//                        value = email,
//                        onValueChange = { email = it },
//                        label = "ooo.tmivdeneg@mail.com",
//                        modifier = Modifier,
//                        Black
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Text(
//                        text = "Отдел",
//                        fontSize = 14.sp,
//                        fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
//                        color = TextGrey,
//                        modifier = Modifier.padding(horizontal = 24.dp)
//
//                    )
//
//                    //  Отдел
//                    CustomTextField(
//                        value = department,
//                        onValueChange = { department = it },
//                        label = "Отдел Андроида",
//                        modifier = Modifier,
//                        Black
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Text(
//                        text = "Должность",
//                        fontSize = 14.sp,
//                        fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
//                        color = TextGrey,
//                        modifier = Modifier.padding(horizontal = 24.dp)
//
//                    )
//
//                    //  Должность
//                    CustomTextField(
//                        position,
//                        {position = it},
//                        "гендир",
//                        modifier = Modifier,
//                        textColor = Black
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Column {
//                        Text(
//                            text = "Пароль",
//                            fontSize = 14.sp,
//                            fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
//                            color = TextGrey,
//                            modifier = Modifier.padding(horizontal = 24.dp)
//                        )
//
//                        //  Пароль
//                        CustomTextField(
//                            value = password,
//                            onValueChange = { password = it },
//                            label = ". . . . . . . . ",
//                            textColor = Black,
//                            modifier = Modifier
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun ShowProfileScreen(){
//    AppTheme {
//        ProfileScreen()
//    }
//}