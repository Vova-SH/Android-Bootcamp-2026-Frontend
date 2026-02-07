package ru.sicampus.bootcamp2026.ui.theme.screens.Profile

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Surface

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileEditScreen(){
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
fun MainProfileContent(
    viewModel: ProfileViewModel = viewModel(),
){
    val uiState by viewModel.uiState.collectAsState()


    var fullName by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var avatarUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(32.dp).fillMaxWidth()
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
                Spacer(modifier = Modifier.height(250.dp))
                ProfileFields("ФИО", fullName ){fullName = it}
                Spacer(modifier = Modifier.height(20.dp))
                ProfileFields("Должность", jobTitle){jobTitle = it}
                Spacer(modifier = Modifier.height(20.dp))
                ProfileFields("Email", email){email = it}
                Spacer(modifier = Modifier.height(20.dp))
                ProfileFields("Текущий пароль", currentPassword) { currentPassword = it }
                Spacer(modifier = Modifier.height(20.dp))
                ProfileFields("Новый пароль", newPassword) { newPassword = it }
            }
        }
        Button(
            onClick = { viewModel.saveChanges(fullName,jobTitle,email,"", currentPassword, newPassword) },
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 30.dp, bottom = 5.dp),
        ){
            Text(
                "Сохранить"
            )
        }
        OutlinedButton(
            onClick = { viewModel.cancelChanges() },
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(2.dp, Blue)
        ){
            Text(
                "Отменить изменения",
                color = Blue
            )
        }
    }
}


@Composable
fun ProfileFields (
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(value) }
    OutlinedTextField(
        value = value,
        textStyle = Typography.labelSmall,
        onValueChange =  onValueChange,
        maxLines = 1,
        label = { Text(label) }
    )
}

@Preview
@Composable
fun ProfileEditScreenPreview(){
    AndroidBootcamp2026FrontendTheme {
        ProfileEditScreen()
    }
}