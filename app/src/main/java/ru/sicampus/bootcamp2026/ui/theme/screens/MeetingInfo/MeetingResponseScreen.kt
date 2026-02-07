package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import ru.sicampus.bootcamp2026.ui.theme.components.userList.UserList
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.domain.entities.UserEntity
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MeetingResponseScreen(
    users: List<UserEntity>,
    appViewModel: AppViewModel

) {
    val viewModel: MeetingInfoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MeetingInfoViewModel(appViewModel) as T
            }
        }
    )
    Scaffold() {
        Column (
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Surface,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(40.dp).fillMaxWidth(),
                    ) {
                        Text(
                            text = "Название",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 150.dp, bottom = 5.dp)
                        )
                        Text(
                            text = "Название встречи",
                            style = Typography.bodyLarge
                        )
                        Text(
                            text = "Описание",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                        )
                        Text(
                            text = "Очень длинное описание предстоящей встречи, которое придумал " +
                                    "сотрудник, чтобы все поняли, для чего она нужна",
                            style = Typography.bodyLarge
                        )
                        Text(
                            text = "Дата и время",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                        )
                        Text(
                            text = "08.02.2026   18:00-19:00",
                            style = Typography.bodyLarge
                        )
                        Text(
                            text = "Место",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                        )
                        Text(
                            text = "Место встречи",
                            style = Typography.bodyLarge
                        )
                        Text(
                            text = "Список участников",
                            style = Typography.labelSmall,
                            modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                        )
                        UserList(
                            fios = users.map { it.fullName },
                            jobTitles = users.map { it.jobTitle },
                        )
                    }
                }
                Image(
                    painter = painterResource(R.drawable.meeting_wave2),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(R.drawable.meeting_wave1),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                IconButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.TopEnd).padding(7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = Color.White,
                        contentDescription = "закрыть"
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
            ) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f).padding(start = 40.dp, end = 12.dp)
                        .fillMaxWidth(),
                ){
                    Text(
                        "Принять"
                    )
                }
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f).padding(start = 12.dp, end = 40.dp)
                        .fillMaxWidth(),
                    border = BorderStroke(2.dp, Blue)
                ){
                    Text(
                        "Отклонить",
                        color = Blue
                    )
                }
            }
        }
    }
}
