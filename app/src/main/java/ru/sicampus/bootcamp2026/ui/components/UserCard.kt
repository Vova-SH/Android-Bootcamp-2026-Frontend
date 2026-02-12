package ru.sicampus.bootcamp2026.ui.components

import android.R.attr.bitmap
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.BitmapImage
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.network.source.ImageLoaderViewModel
import ru.sicampus.bootcamp2026.domain.entities.UserEntity

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UserEntity.CardInList(
    modifier: Modifier = Modifier,
    imageLoaderViewModel: ImageLoaderViewModel = viewModel()
) {
    LaunchedEffect(avatarUrl) {
        imageLoaderViewModel.loadImage(avatarUrl)
    }

    Card(modifier) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Используем состояние из ViewModel
                    imageLoaderViewModel.userAvatar.value?.asImageBitmap()?.let { bitmap ->
                        Image(
                            bitmap,
                            "avatar",
                            Modifier
                                .clip(CircleShape)
                                .size(48.dp)
                        )
                    } ?: Image(
                        Icons.Filled.AccountCircle,
                        "avatar",
                        Modifier
                            .clip(CircleShape)
                            .size(48.dp)
                    )
                    Spacer(Modifier.padding(10.dp))
                    Text("$surname $name ${patronymic ?: ""}")
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
//            ToggleButton(
//                checked = user in currentUser.friends,
//                onCheckedChange = {
//                    if (user in currentUser.friends) {
//                        currentUser.addFriend(user)
//                    } else {
//                        currentUser.removeFriend(user)
//                    }
//                }
//            ) {
//                Icon(
//                    Icons.Filled.Star,
//                    "like"
//                )
//            }
        }
    }
}

