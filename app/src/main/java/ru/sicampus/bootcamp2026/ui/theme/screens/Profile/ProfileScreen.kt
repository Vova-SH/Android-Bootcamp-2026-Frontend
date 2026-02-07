@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package ru.sicampus.bootcamp2026.ui.theme.screens.Profile


import android.annotation.SuppressLint
import android.icu.text.SymbolTable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.components.BottomNavBar
import ru.sicampus.bootcamp2026.ui.theme.screens.Login.LoginViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    appViewModel: AppViewModel,
    userPreferences: UserPreferences
){
    val viewModel: ProfileViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(appViewModel,
                    userPreferences) as T
            }
        }
    )

    val state by viewModel.uiState.collectAsState()

    when(val currentState = state){
        is ProfileState.Error ->ProfileError(currentState, onRefresh = {viewModel.getData()})
        is ProfileState.Loading -> ProfileLoading()
        is ProfileState.EdContent -> ProfileEdContent()
        is ProfileState.NoEdContent -> ProfileNoEdContent(
            appViewModel,
            toInvitations = { viewModel.toInvitations() },
            toTimeTable = { viewModel.toTimeTable() },
             "Профиль",
            userPreferences
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileEdContent(){
    Scaffold(
        contentColor = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {

            MainProfileContent()


            Image(
                painter = painterResource(R.drawable.profile_wave2),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
            Image(
                painter = painterResource(R.drawable.profile_wave1),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Редактирование",
                    modifier = Modifier
                        .padding(20.dp),
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Image(
                    painter = painterResource(id = R.drawable.sample_avatar),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(144.dp)
                        .clip(CircleShape)

                )
            }
        }
    }
}

@Composable
fun MainProfileViewContent(
    viewModel: ProfileViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    val data = uiState as ProfileState.NoEdContent
    Column(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxWidth()
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(22.dp)
            ){
                Text(
                    text = "ФИО",
                    style = Typography.labelSmall,
                    modifier = Modifier.paddingFromBaseline(top = 270.dp, bottom = 5.dp)
                )
                Text(
                    text = data.fullName,
                    style = Typography.bodyLarge
                )
                Text(
                    text = "Должность",
                    style = Typography.labelSmall,
                    modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                )
                Text(
                    text = data.jobTitle,
                    style = Typography.bodyLarge
                )
                Text(
                    text = "Email",
                    style = Typography.labelSmall,
                    modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                )
                Text(
                    text = data.email,
                    style = Typography.bodyLarge
                )
            }
        }
        OutlinedButton(
            onClick = {viewModel.switchToEditMode(fio = "", jobTitle = "", email = "", password = "")},
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            border = BorderStroke(3.dp, Blue)
        ){
            Text(
                "Изменить профиль",
                color = Blue
            )
        }


    }
}



@Composable
fun ProfileError(
    state: ProfileState.Error,
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
fun ProfileLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun ProfileNoEdContent(
    appViewModel: AppViewModel,
    toInvitations: () -> Unit,
    toTimeTable: () -> Unit,
    selectedItem: String,
    userPreferences: UserPreferences
){
    val viewModel: ProfileViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(appViewModel,
                    userPreferences) as T
            }
        }
    )
    var selectedItem by remember { mutableStateOf("Профиль") }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                appViewModel = appViewModel
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {

            MainProfileViewContent()

            Image(
                painter = painterResource(R.drawable.profile_wave2),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
            Image(
                painter = painterResource(R.drawable.profile_wave1),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Профиль",
                    modifier = Modifier
                        .padding(20.dp),
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Image(
                    painter = painterResource(id = R.drawable.sample_avatar),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(144.dp)
                        .clip(CircleShape)

                )
            }
        }
    }

}

