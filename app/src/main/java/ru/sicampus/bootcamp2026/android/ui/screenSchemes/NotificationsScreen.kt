package ru.sicampus.bootcamp2026.android.ui.screenSchemes
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import ru.sicampus.bootcamp2026.R
//import ru.sicampus.bootcamp2026.android.ui.theme.AppTheme
//import ru.sicampus.bootcamp2026.android.ui.theme.Black
//import ru.sicampus.bootcamp2026.android.ui.theme.TextGrey
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun NotificationScreen() {
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Уведомления",
//                        fontSize = 20.sp,
//                        fontFamily = FontFamily(Font(R.font.open_sans_bold)),
//                        modifier = Modifier
//                            .padding(start = 45.dp)
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { /* действие */ }) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.close_icon),
//                            contentDescription = "Закрыть",
//                            tint = Black
//                        )
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 18.dp, top = 30.dp)
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//        ) {
//
//            Spacer(modifier = Modifier.height(176.dp))
//
//            Icon(
//                painter = painterResource(R.drawable.no_notifications_icon),
//                contentDescription = "Нет уведомлений",
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//            )
//
//            Spacer(modifier = Modifier.height(48.dp))
//
//            Text(
//                text = "У вас пока нет уведомлений",
//                fontSize = 16.sp,
//                fontFamily = FontFamily(Font(R.font.open_sans_semibold)),
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally),
//                color = TextGrey
//            )
//
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun ShowNotificationsScreen() {
//    AppTheme {
//        NotificationScreen()
//    }
//}