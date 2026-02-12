package ru.sicampus.bootcamp2026.ui.screens.profile


import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.serialization.Contextual
import ru.sicampus.bootcamp2026.App
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.login.LoginActivity
import ru.sicampus.bootcamp2026.ui.root.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.root.theme.BlueMain
import ru.sicampus.bootcamp2026.ui.root.theme.GrayTextColor
import ru.sicampus.bootcamp2026.ui.root.theme.Typography
import ru.sicampus.bootcamp2026.utils.SettingsUtils

@Composable
fun ProfileScreen(
    context: Context,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory.create(context))
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(BackgroundColor),) {
        when (uiState) {
            is ProfileState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            is ProfileState.Data -> Content(viewModel)
            is ProfileState.EditData -> EditContent(viewModel)
            is ProfileState.Error -> ErrorContent(viewModel)
            is ProfileState.Search -> SearchContent(viewModel)
        }
    }
}

@Composable
fun Content(viewModel: ProfileViewModel) {
    val state by viewModel.state.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    val searchText = remember { mutableStateOf("") }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = ""
                )
            },
            value = searchText.value,
            onValueChange = { searchText.value = it },
            label = {
                Text(
                    text = stringResource(R.string.search),
                    style = Typography.bodyLarge
                )
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth(0.95f)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    searchBtnHandler(viewModel)
                },
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.load() },
        ) {
            LazyColumn(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(Modifier.size(20.dp))
                    Card(
                        modifier = Modifier.size(150.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(18.dp)
                    ) {
                        AsyncImage(
                            state.photoUrl,
                            null,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxHeight()
                        )
                    }
                    Spacer(Modifier.size(5.dp))
                    Text(
                        state.fullName, fontSize = 20.sp, color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        state.email, fontSize = 15.sp, color = Color(0xff636363),
                        fontWeight = FontWeight.Normal
                    )
                    Box(
                        Modifier

                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 20.dp)
                            .shadow(4.dp, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White)
                    ) {
                        Column {
                            if (state.position != null && state.position != "") {
                                Column(
                                    Modifier.padding(
                                        top = 15.dp,
                                        start = 25.dp,
                                        bottom = 10.dp
                                    )
                                ) {
                                    Text(
                                        state.position!!, fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        stringResource(R.string.post),
                                        fontSize = 14.sp,
                                        color = Color(0xff636363),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(Modifier.size(4.dp))
                                }
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                            }

                            if (state.department != null && state.department != "") {
                                Column(
                                    Modifier.padding(
                                        top = 15.dp,
                                        start = 25.dp,
                                        bottom = 10.dp
                                    )
                                ) {
                                    Text(
                                        state.department!!, fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        stringResource(R.string.department),
                                        fontSize = 14.sp,
                                        color = Color(0xff636363),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(Modifier.size(4.dp))
                                }
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                            }

                            if (state.description != null && state.description != "") {
                                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                                    Spacer(Modifier.size(10.dp))
                                    Text(
                                        state.description!!,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.size(5.dp))
                                    Text(
                                        stringResource(R.string.about),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xff636363)
                                    )
                                    Spacer(modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }

                    Card(
                        onClick = {
                            viewModel.startUprate()
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(0.95f)
                            .clip(RoundedCornerShape(15.dp))
                            .border(
                                1.dp,
                                color = Color(0xff2F458B),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.edit),
                                color = Color(0xff0A266C),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(Modifier.size(15.dp))
                    Card(
                        onClick = {
                            SettingsUtils(App.context).clear()
                            App.context.startActivity(
                                Intent(App.context, LoginActivity::class.java).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                            )

                        }, modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(0.95f)
                            .clip(
                                RoundedCornerShape(15.dp)
                            ), colors = CardDefaults.cardColors(containerColor = Color(0xffFFBBBB))
                    ) {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(R.drawable.exit_icon), "",
                                tint = Color(0xffD50000)
                            )
                            Spacer(Modifier.size(5.dp))
                            Text(
                                stringResource(R.string.logout), color = Color(0xffD50000),
                                style = Typography.bodyLarge, fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun EditContent(viewModel: ProfileViewModel) {
    val state by viewModel.state.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    val searchText = remember { mutableStateOf("") }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = ""
                )
            },
            value = searchText.value,
            onValueChange = { searchText.value = it },
            label = {
                Text(
                    text = stringResource(R.string.search),
                    style = Typography.bodyLarge
                )
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth(0.95f)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    searchBtnHandler(viewModel)
                },
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = BackgroundColor,
                disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Spacer(Modifier.size(20.dp))
                Card(
                    modifier = Modifier.size(150.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(18.dp)
                ) {
                    AsyncImage(
                        state.photoUrl,
                        null,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxHeight()
                    )
                }
                Spacer(Modifier.size(8.dp))

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 16.dp)
                        .shadow(4.dp, RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.White)
                ) {
                    Column {
                        Spacer(Modifier.size(16.dp))
                        CustomTextField8(
                            value = state.updateFirstName,
                            onValueChange = { viewModel.onFirstNameChange(it) },
                            placeholder = "Имя",
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .shadow(3.dp, RoundedCornerShape(15.dp)),
                            height = 50
                        )
                        Spacer(Modifier.size(10.dp))
                        CustomTextField8(
                            value = state.updateSecondName,
                            onValueChange = { viewModel.onSecondNameChange(it) },
                            placeholder = "Фамилия",
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .shadow(3.dp, RoundedCornerShape(15.dp)),
                            height = 50
                        )
                        Spacer(Modifier.size(10.dp))
                        CustomTextField8(
                            value = state.updatePosition,
                            onValueChange = { viewModel.onPositionChange(it) },
                            placeholder = "Должность",
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .shadow(3.dp, RoundedCornerShape(15.dp)),
                            height = 50
                        )
                        Spacer(Modifier.size(10.dp))
                        CustomTextField8(
                            value = state.updateDepartment,
                            onValueChange = { viewModel.onDepartmentChange(it) },
                            placeholder = "Отдел",
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .shadow(3.dp, RoundedCornerShape(15.dp)),
                            height = 50
                        )
                        Spacer(Modifier.size(10.dp))
                        CustomTextField8(
                            value = state.updateDescription,
                            onValueChange = { viewModel.onDescriptionChange(it) },
                            placeholder = "О себе",
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .shadow(3.dp, RoundedCornerShape(15.dp)),
                            height = 50
                        )
                        Spacer(Modifier.size(16.dp))
                    }
                }

                Card(
                    onClick = {
                        viewModel.update()
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(0.95f)
                        .clip(RoundedCornerShape(15.dp))
                        .border(1.dp, color = Color(0xff2F458B), shape = RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Обновить",
                            color = Color(0xff0A266C),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.size(15.dp))
                Card(
                    onClick = {
                        SettingsUtils(App.context).clear()
                        App.context.startActivity(
                            Intent(App.context, LoginActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        )

                    }, modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(0.95f)
                        .clip(
                            RoundedCornerShape(15.dp)
                        ), colors = CardDefaults.cardColors(containerColor = Color(0xffFFBBBB))
                ) {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(R.drawable.exit_icon), "",
                            tint = Color(0xffD50000)
                        )
                        Spacer(Modifier.size(5.dp))
                        Text(
                            stringResource(R.string.logout), color = Color(0xffD50000),
                            style = Typography.bodyLarge, fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(Modifier.size(20.dp))
            }

        }
    }
}

@Composable
fun ErrorContent(viewModel: ProfileViewModel) {
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    val state by viewModel.state.collectAsState()

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = swipeRefreshState,
        onRefresh = { viewModel.load() },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.errorMessage ?: "Обновите экран",
                color = Color.Red,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(0.9f)
            )
        }
    }
}

@Composable
fun SearchContent(
    viewModel: ProfileViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = ""
                )
            },
            value = state.search,
            onValueChange = { viewModel.onSearchChange(it) },
            label = {
                Text(
                    text = stringResource(R.string.search),
                    style = Typography.bodyLarge
                )
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth(0.95f)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    searchBtnHandler(viewModel)
                }
        )
    }
}

private fun searchBtnHandler(viewModel: ProfileViewModel) {
    viewModel.startSearch()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextField8(
    value: String?,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    height: Int
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val elevation by animateDpAsState(
        targetValue = if (isFocused) 8.dp else 0.dp,
        animationSpec = tween(durationMillis = 200)
    )

    Surface(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(15.dp)
            )
            .border(
                width = 1.dp,
                color = if (isFocused) BlueMain.copy(alpha = 0.6f)
                else Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))
            .background(
                color = if (enabled) Color.White else Color.LightGray
            )
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusRequester.requestFocus() },
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Spacer(modifier = Modifier.width(1.dp))

            BasicTextField(
                value = value ?: "",
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .height(height.dp)
                    .fillMaxWidth(0.9f),
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = TextStyle(
                    color = if (enabled) Color.Black else Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (height != 110) Alignment.CenterStart else Alignment.TopStart
                    ) {
                        if (value?.isEmpty() ?: true) {
                            Text(
                                text = placeholder,
                                color = GrayTextColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}
