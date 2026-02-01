package ru.sicampus.bootcamp2026.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.Green
import ru.sicampus.bootcamp2026.ui.theme.Grey82
import ru.sicampus.bootcamp2026.ui.theme.GreyForCard
import ru.sicampus.bootcamp2026.ui.theme.Orange
import ru.sicampus.bootcamp2026.ui.theme.Red

@Composable
fun CardEmployeeGreen() {
    Row(
        modifier = Modifier
            .width(346.dp)
            .height(122.dp)
            .background(color = GreyForCard, shape = RoundedCornerShape(18.dp)) // TODO сменить дизайн, а то на тёмной теме его совсем не видно
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "фото",
                    modifier = Modifier
                        .padding(end = 8.dp, start = 10.dp)
                        .width(30.dp)
                        .height(30.dp)
                )
                Text("Куприн Иван Васильевич", fontWeight = FontWeight.W600, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Телефон:",
                    color = Grey82,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(end = 6.dp, start = 10.dp)
                )
                Text(
                    text = "8 (000)-00-01",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Почта:",
                    color = Grey82,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(end = 6.dp, start = 10.dp)
                )
                Text(
                    text = "ivan@it.ru",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier.padding(top = 20.dp, start = 70.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Ещё",
                    tint = Color.Black
                )
            }
            Text(
                text = "Согласился",
                color = Green,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(top = 80.dp),
                maxLines = 1
            )
        }
    }
}
@Composable
fun CardEmployeeRed() {
    Row(
        modifier = Modifier
            .width(346.dp)
            .height(122.dp)
            .background(color = GreyForCard, shape = RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "фото",
                    modifier = Modifier
                        .padding(end = 8.dp, start = 10.dp)
                        .width(30.dp)
                        .height(30.dp)
                )
                Text("Куприн Иван Васильевич", fontWeight = FontWeight.W600, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Телефон:",
                    color = Grey82,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(end = 6.dp, start = 10.dp)
                )
                Text(
                    text = "8 (000)-00-01",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Почта:",
                    color = Grey82,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(end = 6.dp, start = 10.dp)
                )
                Text(
                    text = "ivan@it.ru",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
        Box(
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier.padding(top = 20.dp, start = 70.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Ещё",
                    tint = Color.Black
                )
            }
            Text(
                text = "Отказался",
                color = Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(top = 80.dp),
                maxLines = 1
            )
        }
    }
}
@Composable
fun CardEmployeeOrange() {
    Row(
        modifier = Modifier
            .width(346.dp)
            .height(122.dp)
            .background(color = GreyForCard, shape = RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "фото",
                    modifier = Modifier
                        .padding(end = 8.dp, start = 10.dp)
                        .width(30.dp)
                        .height(30.dp)
                )
                Text("Куприн Иван Васильевич", fontWeight = FontWeight.W600, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Телефон:",
                    color = Grey82,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(end = 6.dp, start = 10.dp)
                )
                Text(
                    text = "8 (000)-00-01",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Почта:",
                    color = Grey82,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(end = 6.dp, start = 10.dp)
                )
                Text(
                    text = "ivan@it.ru",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
        Box(
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier.padding(top = 20.dp, start = 70.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Ещё",
                    tint = Color.Black
                )
            }
            Text(
                text = "Ждём ответа",
                color = Orange,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(top = 80.dp),
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun ShowCard() {
    CardEmployeeGreen()
}