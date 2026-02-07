package ru.sicampus.bootcamp2026


import android.provider.ContactsContract
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.Typography
import kotlin.math.sin

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val searchText = remember { mutableStateOf("") }
    Box(Modifier
        .fillMaxSize()
        .background(Color(0xffEEEEEE))){
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                leadingIcon = { Icon(Icons.Outlined.Search, "") },
                value = searchText.value,
                onValueChange = { searchText.value = it },
                label = {
                    Text(
                        "Поиск пользователей", style =
                            Typography.bodyLarge, color = Color(0xff49454F)
                    )
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.height(55.dp).fillMaxWidth(0.95f)
            )
            Spacer(Modifier.size(20.dp))
            Box(
                Modifier.size(155.dp).background(
                    color = Color(0xff54E68C), shape = CircleShape
                ), contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(R.drawable.iconperson), "", Modifier.size(130.dp))

            }
            Spacer(Modifier.size(5.dp))
            Text(
                "Алексей Петров", fontSize = 20.sp, color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                "alexpetrov@bk.ru", fontSize = 15.sp, color = Color(0xff636363),
                fontWeight = FontWeight.Normal
            )

        }}
}


@Preview
@Composable
private fun CCC() {
    ProfileScreen()
}