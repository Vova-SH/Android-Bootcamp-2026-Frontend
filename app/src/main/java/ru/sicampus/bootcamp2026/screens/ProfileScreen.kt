package ru.sicampus.bootcamp2026.screens

import androidx.compose.animation.animateBounds
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.components.ProfileField
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Text("Профиль", modifier = Modifier.fillMaxSize().wrapContentHeight())

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val fraction = scrollBehavior.state.collapsedFraction

    var isEditable by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Имя пользователя",
                        color = White,
                        fontSize = lerp(24.sp, 20.sp, fraction),
                        fontFamily = FontFamily(androidx.
                        compose.ui.text.font.Font(R.font.montserrat_bold))
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Blue
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = White
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.default_im),
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(lerp(390.dp, 0.dp, fraction))
                        .alpha(1f - fraction)

                )

                FloatingActionButton(onClick = {},
                    containerColor = Blue,
                    modifier = Modifier
                        .height(85.dp)
                        .width(85.dp)
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                        .alpha(1f - fraction)
                        .offset(x = (-16.dp), y = 350.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Icon(painter = painterResource(R.drawable.camera),
                        contentDescription = "Смена изображения",
                        tint = Color.White)
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(padding)
                        .padding(horizontal = 24.dp)
                        .offset( y = lerp(240.dp, 6.dp, fraction))
                ) {
                    ProfileField("Телефон", "77889990000990", isEditable = isEditable)
                    ProfileField("Email", "email@gmail.com", isEditable = isEditable)
                    ProfileField("Информация", "Hello world Hello world " +
                            "Hello world Hello world Hello world Hello world Hello world " +
                            "Hello world Hello world Hello world Hello world Hello world " +
                            "Hello world Hello world", isEditable = isEditable)
                }
            }

        }
    }
}

@Preview
@Composable
fun Show4() {
    ProfileScreen()
}