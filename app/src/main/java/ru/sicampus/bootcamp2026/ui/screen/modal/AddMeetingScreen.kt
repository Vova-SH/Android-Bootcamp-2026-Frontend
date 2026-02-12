package ru.sicampus.bootcamp2026.ui.screen.modal

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.components.AppButton
import ru.sicampus.bootcamp2026.components.AppTopBar
import ru.sicampus.bootcamp2026.components.InputField
import ru.sicampus.bootcamp2026.components.UserItem
import ru.sicampus.bootcamp2026.ui.screen.meetings.EmployeeListIntent
import ru.sicampus.bootcamp2026.ui.screen.meetings.EmployeeSearchState
import ru.sicampus.bootcamp2026.ui.theme.BgGradientBottom
import ru.sicampus.bootcamp2026.ui.theme.BgGradientTop
import ru.sicampus.bootcamp2026.ui.theme.SecondaryGray
import ru.sicampus.bootcamp2026.ui.theme.White
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun AddMeetingScreen(
    viewModel: AddMeetingViewModel = viewModel(),
    onClose: suspend () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedUsernames by viewModel.selectedUsernames.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    val lazyListState = rememberLazyListState()
    val nestedScrollConnection = rememberNestedScrollConnection(scrollState, lazyListState)
    val scope = rememberCoroutineScope()
    val isFormValid = viewModel.isFormValid

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(White)
            .nestedScroll(nestedScrollConnection)
    ) {
        AppTopBar(
            title = "Новая встреча",
            startIconId = R.drawable.ic_close,
            startIconButtonOnClick = { scope.launch { onClose() } },
            endIconId = R.drawable.ic_reload,
            endIconButtonOnClick = { viewModel.onIntent(EmployeeListIntent.Refresh) }
        )

        MainScrollableContent(
            scrollState = scrollState,
            lazyListState = lazyListState,
            uiState = state,
            viewModel = viewModel
        )
        Log.d("KTOR", "Selected usernames: $selectedUsernames")
        AddMeetingFooter(
            modifier = Modifier.align(Alignment.BottomCenter),
            onSave = {
                viewModel.createMeeting(
                    onSuccess = {
                        scope.launch { onClose() }
                        viewModel.clearSelectedUsernames()
                    }
                )
            },
            isEnabled = isFormValid
        )
    }
}
@Composable
private fun MainScrollableContent(
    scrollState: ScrollState,
    lazyListState: LazyListState,
    uiState: EmployeeSearchState,
    viewModel: AddMeetingViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(112.dp))

        ContentAboveLastItem(viewModel)

        LastItemContainer(
            lazyListState = lazyListState,
            state = uiState,
            viewModel = viewModel
        )
    }
}

@Composable
private fun ContentAboveLastItem(viewModel: AddMeetingViewModel) {
    val context = LocalContext.current

    InputField(
        title = "Название",
        state = viewModel.titleState,
        placeholderText = "Как бы Вы назвали встречу?",
    )
    InputField(
        title = "Место",
        state = viewModel.locationState,
        placeholderText = "Где будет проходить встреча?",
    )
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { showDateTimePicker(context, viewModel.dateState, viewModel.timeState) }
        ) {
            InputField(
                title = "Дата",
                state = viewModel.dateState,
                iconId = R.drawable.ic_date,
                enabled = false
            )
            Box(modifier = Modifier
                .matchParentSize()
                .zIndex(1f)
                .clickable {
                    showDateTimePicker(context, viewModel.dateState, viewModel.timeState)
                })
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            InputField(
                title = "Время",
                state = viewModel.timeState,
                iconId = R.drawable.ic_time,
                enabled = false
            )
            Box(modifier = Modifier
                .matchParentSize()
                .zIndex(1f)
                .clickable {
                    showDateTimePicker(context, viewModel.dateState, viewModel.timeState)
                })
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun LastItemContainer(
    lazyListState: LazyListState,
    state: EmployeeSearchState,
    viewModel: AddMeetingViewModel
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topOffset = screenHeight * 0.06f
    val modalHeight = screenHeight - topOffset
    val appTopBarHeight = 132.dp
    val lazyHeight = modalHeight - appTopBarHeight + 16.dp

    val selectedUsernames by viewModel.selectedUsernames.collectAsState()

    val isNeededLoadMore by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemsCount = layoutInfo.totalItemsCount

            totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 10
        }
    }

    LaunchedEffect(isNeededLoadMore) {
        if (isNeededLoadMore) {
            viewModel.onIntent(EmployeeListIntent.LoadMore)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LastItemContainerTopBar(viewModel.searchState)

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = lazyHeight)
        ) {
            item { Spacer(modifier = Modifier.height(92.dp)) }

            when (state) {
                is EmployeeSearchState.Loading -> {
                    item { ItemLoading() }
                }
                is EmployeeSearchState.Error -> {
                    item { ItemError { viewModel.onIntent(EmployeeListIntent.Refresh) } }
                }
                is EmployeeSearchState.Content -> {
                    items(state.users) { item ->
                        if (item is EmployeeSearchState.Item.Employee) {
                            val isSelected = selectedUsernames.contains(item.entity.username)

                            UserItem(
                                user = item.entity,
                                isSelected = isSelected,
                                onCheckChange = {
                                    viewModel.toggleUserSelection(item.entity.username)
                                }
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(start = 96.dp, end = 16.dp),
                                thickness = 1.dp,
                                color = SecondaryGray
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(116.dp)) }
        }
    }
}

@Composable
fun ItemLoading(
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Загрузка...",
        )
    }
}

@Composable
fun ItemError(onRefresh:() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Ошибка загрузки",
            )
            AppButton(
                text = "Повторить",
                onClick = { onRefresh() },
                enabled = true
            )
        }
    }
}

@Composable
private fun LastItemContainerTopBar(searchState: TextFieldState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = BgGradientTop)
            .padding(bottom = 8.dp)
            .zIndex(1f)
    ) {
        InputField(
            state = searchState,
            modifier = Modifier
                .fillMaxWidth(),
            title = "Участники",
            placeholderText = "Кого бы Вы позвали на встречу?"
        )
    }
}

@Composable
private fun AddMeetingFooter(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    isEnabled: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(116.dp)
            .background(brush = BgGradientBottom)
            .padding(top = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AppButton(
            text = "Создать",
            onClick = onSave,
            enabled = isEnabled
        )
    }
}

@Composable
private fun rememberNestedScrollConnection(
    scrollState: ScrollState,
    lazyListState: LazyListState
): NestedScrollConnection {
    return remember(scrollState, lazyListState) {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                if (delta < 0) {
                    val columnCanScroll = scrollState.value < scrollState.maxValue
                    return if (columnCanScroll) {
                        val consumed = scrollState.dispatchRawDelta(-delta)
                        Offset(0f, -consumed)
                    } else {
                        val consumed = lazyListState.dispatchRawDelta(-delta)
                        Offset(0f, -consumed)
                    }
                }
                if (delta > 0) {
                    val lazyCanScrollUp = lazyListState.firstVisibleItemIndex > 0 ||
                            lazyListState.firstVisibleItemScrollOffset > 0
                    return if (lazyCanScrollUp) {
                        val consumed = lazyListState.dispatchRawDelta(-delta)
                        Offset(0f, -consumed)
                    } else {
                        val consumed = scrollState.dispatchRawDelta(-delta)
                        Offset(0f, -consumed)
                    }
                }
                return Offset.Zero
            }
        }
    }
}
@SuppressLint("DefaultLocale")
fun showDateTimePicker(context: Context, dateState: TextFieldState, timeState: TextFieldState) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            dateState.edit { replace(0, length, date) }

            TimePickerDialog(
                context,
                { _, hourOfDay, _ ->
                    val time = String.format("%02d:00", hourOfDay)
                    timeState.edit { replace(0, length, time) }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                0,
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}