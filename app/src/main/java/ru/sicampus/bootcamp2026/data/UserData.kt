package ru.sicampus.bootcamp2026.data

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.MeetingData.InvitationState.Agree
import ru.sicampus.bootcamp2026.data.MeetingData.InvitationState.Disagree
import ru.sicampus.bootcamp2026.data.MeetingData.InvitationState.NoAnswer

data class UserData (
    var surname: String = "",
    var name: String = "",
    var patronymic: String? = null,
    var avatar: ImageBitmap? = null,
    var telephone: String = "",
    var mail: String = "",
    var contacts: MutableMap<String, String> = mutableMapOf(),
    var friends: MutableList<UserData> = mutableListOf()
)  {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun CardInList(modifier : Modifier = Modifier, currentUser : UserData) {
        val user = this
        Card(
            modifier
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (avatar != null) {
                            Image(
                                avatar!!,
                                "avatar",
                                Modifier
                                    .clip(CircleShape)
                                    .size(48.dp)
                            )
                        } else {
                            Image(
                                Icons.Filled.AccountCircle,
                                "avatar"
                            )
                        }
                        Spacer(Modifier.padding(10.dp))
                        Text("$surname $name ${patronymic ?: ""}")
                    }
                    Spacer(Modifier.padding(5.dp))
                    Row {
                        Text(
                            "Телефон: ",
                            color = Color.Gray
                        )
                        Text(telephone)
                    }
                    Spacer(Modifier.padding(5.dp))
                    Row {
                        Text(
                            "Почта: ",
                            color = Color.Gray
                        )
                        Text(mail)
                    }
                    Spacer(Modifier.padding(5.dp))
                }
                ToggleButton(
                    checked = user in currentUser.friends,
                    onCheckedChange = {
                        if (user in currentUser.friends) {
                            currentUser.friends.remove(user)
                        } else {
                            currentUser.friends.add(user)
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.Star,
                        "like"
                    )
                }
            }
        }
    }

    @Composable
    fun CardInMeeting () {

    }

}

@Preview
@Composable
fun CardInListPreview() {
    val user = UserData(
        surname = "Иванов",
        name = "Иван",
        patronymic = "Иванович",
        telephone = "+79111234567",
        mail = "ivanov@example.com"
    )


    user.contacts["telegram"] = "@asfasff"
    user.contacts["vk"] = "safafa"
    user.contacts["gsggs"] = "sdasg"

    user.avatar = ImageBitmap.imageResource(R.drawable.img_1);
    user.CardInList(Modifier.fillMaxWidth().padding(10.dp), UserData())
}