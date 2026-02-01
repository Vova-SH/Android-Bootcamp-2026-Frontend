package ru.sicampus.bootcamp2026.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.data.MeetingData.InvitationState.*
import java.time.LocalDateTime

data class MeetingData(
    var name : String,
    var description : String,
    var time: LocalDateTime,
    var users : MutableMap<UserData, InvitationState> = mutableMapOf(),
    var admins : MutableList<UserData> = mutableListOf(),
    var creator: UserData
) {
    enum class InvitationState {
        Agree,
        Disagree,
        NoAnswer
    }



    @Composable
    fun ClassicCard(currentUser: UserData, modifier: Modifier = Modifier) {
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
                    Text("" + time.dayOfMonth + "." + time.monthValue + "." + time.year)
                }
                Spacer(Modifier.padding(5.dp))
                Row {
                    Text("Время: ",
                        color = Color.Gray)
                    Text("" + time.hour + ":00")
                }
                Spacer(Modifier.padding(5.dp))
                if (currentUser in admins) {
                    Text("Вы модератор данной встречи")
                } else if (currentUser == creator) {
                    Text("Вы создатель данной встречи")
                } else {
                    Log.d("us", users.toString())
                    Log.d("u", (currentUser in users).toString())
                    when (users[currentUser]) {
                        Agree -> Text(
                            "Вы согласились",
                            color = Color(0xFF4CAF50)
                        )

                        Disagree -> Text(
                            "Вы отказались",
                            color = Color.Red
                        )

                        NoAnswer -> Row {
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ClassicCardPreview() {
    var meetingData = MeetingData(
        "Встреча 1",
        "Это первая встреча",
        time = LocalDateTime.now(),
        creator = UserData("Admin"),
        users = mutableMapOf(
            UserData("1") to NoAnswer,
            UserData("2") to Agree,
            UserData("3") to Disagree
        )
    )
    Column {
        meetingData.ClassicCard(UserData("1"), Modifier.fillMaxWidth())
        meetingData.ClassicCard(UserData("2"), Modifier.fillMaxWidth())
        meetingData.ClassicCard(UserData("3"), Modifier.fillMaxWidth())
    }
}