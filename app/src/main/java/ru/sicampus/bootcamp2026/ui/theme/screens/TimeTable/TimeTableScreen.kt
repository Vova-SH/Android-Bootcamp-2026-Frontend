package ru.sicampus.bootcamp2026.ui.theme.screens.TimeTable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember  // ← remember с маленькой r
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.*
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.components.BottomNavBar
import ru.sicampus.bootcamp2026.ui.theme.components.WeekView
import ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo.MeetingInfoViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*


import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.data.dto.MeetinCreateDTO
import ru.sicampus.bootcamp2026.data.source.MeetingCreateNetDataSource

import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.InverseSurface
import ru.sicampus.bootcamp2026.ui.theme.LightBlue
import ru.sicampus.bootcamp2026.ui.theme.OpDeepBlue
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.fontFamily
import kotlinx.coroutines.launch




@SuppressLint("NewApi")
@Composable
fun TimetableScreen(
    appViewModel: AppViewModel,
    userPreferences: UserPreferences
    ) {
    val viewModel: TTViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TTViewModel(appViewModel) as T
            }
        }
    )

    val state by viewModel.uiState.collectAsState()

    when(val currState = state){
        is TTState.Error -> TimetableError(currState, onRefresh = {viewModel.getData()})
        is TTState.Loading -> TimetableLoading()
        is TTState.Content -> TimeTableContent(appViewModel,toInvitations = {viewModel.toInvitations()}, toProfile = {viewModel.toProfile()} )
    }
}


@Composable
fun TimetableError(
    state: TTState.Error,
    onRefresh: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(
                onClick = onRefresh
            ) {
                Text("refresh")
            }
        }
    }
}


@Composable
fun TimetableLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun TimeTableContent(
    appViewModel: AppViewModel,
    toInvitations: () -> Unit,
    toProfile: () -> Unit
){
    val viewModel: TTViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TTViewModel(appViewModel) as T
            }
        }
    )

    var selectedItem by remember { mutableStateOf("Расписание") }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Неделя", "Месяц")



    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                appViewModel = appViewModel
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 50.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                options.forEachIndexed { index, label ->
                    Button(
                        onClick = { selectedIndex = index },
                        colors = if (index == selectedIndex)
                            ButtonDefaults.buttonColors()
                        else
                            ButtonDefaults.outlinedButtonColors(),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(text = label)
                    }
                }
            }

            when (selectedIndex) {
                0 -> WeekView(appViewModel = appViewModel, viewModel = viewModel)
                //1 -> MonthView()
            }
    }


    }
}







