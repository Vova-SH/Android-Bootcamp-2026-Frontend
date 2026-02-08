package ru.sicampus.bootcamp2026

//import ru.sicampus.bootcamp2026.ui.screen.meetings.MeetingsScreen
//import ru.sicampus.bootcamp2026.ui.screen.meeting.Meeting
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme

var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0eWdoQGJrLnJ1IiwiaWF0IjoxNzcwNTY2MDY5LCJleHAiOjE3NzA1Njk2Njl9.1-peDdxCOW-PyMYpSUk6hWu0POuwYmMOQK1MwCuxBK4"

var currentUser: UserEntity? = null // TODO в дальнейшем заменить на авторизацию
var selectedUser: UserEntity? = null
var selectedMeeting : MeetingEntity? = null
var historyOfUserSearch = mutableListOf<String>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme {
                ShowScreen()
            }
        }
    }
}
