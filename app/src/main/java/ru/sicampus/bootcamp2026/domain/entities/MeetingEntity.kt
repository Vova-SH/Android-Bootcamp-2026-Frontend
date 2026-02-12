package ru.sicampus.bootcamp2026.domain.entities

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime

data class MeetingEntity(
    val name: String = "",
    val employeeAdmin: String = "" ,
    val startBooking: LocalDateTime = LocalDateTime.now(),
    val endBooking : LocalDateTime = LocalDateTime.now(),
    val invited : List<String> = listOf(),
    val approval : Boolean? = null
) {

    @Composable
    fun ClassicCard(currentUser: UserEntity, modifier: Modifier = Modifier) {
        Card {
            Column(modifier.padding(10.dp)) {
                Row {
                    Text("Название: ",
                        color = Color.Gray)
                    Text(name)
                }
                Spacer(Modifier.padding(5.dp))
                Row {
                    Text("Дата: ",
                        color = Color.Gray)
                    Text("" + startBooking.dayOfMonth + "." + startBooking.monthValue + "." + startBooking.year)
                }
                Spacer(Modifier.padding(5.dp))
                Row {
                    Text("Время начала: ",
                        color = Color.Gray)
                    Text("" + startBooking.hour + ":00")
                }
                Row {
                    Text("Время конца: ",
                        color = Color.Gray)
                    Text("" + endBooking.hour + ":00")
                }
                Spacer(Modifier.padding(5.dp))
                 if (currentUser.FIO() + " ${currentUser.mail}" == employeeAdmin) {
                    Text("Вы создатель данной встречи")
                } else {
                    Log.d("us", currentUser.toString())
                    when (approval) {
                        true -> Text(
                            "Вы согласились",
                            color = Color(0xFF4CAF50)
                        )

                        false -> Text(
                            "Вы отказались",
                            color = Color.Red
                        )

                        null -> Row {
                            Button(
                                {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50)
                                )
                            ) { Text("Согласиться") }
                            Spacer(Modifier.padding(8.dp))
                            Button(
                                {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) { Text("Отказаться") }
                        }

                        null -> {
                            Text("Error: непредвиденное обстоятельсво", color = Color.Red, textDecoration = TextDecoration.Underline)
                        }
                    }
                }
            }
        }
    }


}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview
//@Composable
//fun ClassicCardPreview() {
//    val meetingEntity = MeetingEntity(
//        id = 1,
//        "Встреча 1",
//        "Это первая встреча",
//        time = LocalDateTime.now(),
//        creator = UserEntity("Admin"),
//        users = mutableMapOf(
//            UserEntity("1") to NoAnswer,
//            UserEntity("2") to Agree,
//            UserEntity("3") to Disagree
//        )
//    )
//    Column {
//        meetingEntity.ClassicCard(UserEntity("1"), Modifier.fillMaxWidth())
//        meetingEntity.ClassicCard(UserEntity("2"), Modifier.fillMaxWidth())
//        meetingEntity.ClassicCard(UserEntity("3"), Modifier.fillMaxWidth())
//    }
//}