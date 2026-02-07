package ru.sicampus.bootcamp2026.ui.theme.components.userList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.Typography

@Composable
fun UserListItem(
    fio: String,
    jobTitle: String,
) {

    Row(
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = R.drawable.sample_avatar),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 6.dp)
                .size(39.dp)
                .clip(CircleShape)
        )
        Column() {
            Text(
                fio,
                Modifier.padding(bottom = 5.dp),
                style = Typography.bodyLarge
            )
            Text(
                jobTitle,
                style = Typography.labelSmall
            )
        }
    }

}

@Composable
fun UserList(
    fios: List<String>,
    jobTitles: List<String>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(fios) { index, fio ->
            UserListItem(
                fio = fio,
                jobTitle = jobTitles[index]
            )
        }
    }
}


@Preview
@Composable
fun UserList() {
    AndroidBootcamp2026FrontendTheme() {
        UserListItem("svnsn", "QA")
    }
}