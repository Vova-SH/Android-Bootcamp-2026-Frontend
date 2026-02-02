package ru.sicampus.bootcamp2026.ui.screens.invites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.components.InvitationCard
import ru.sicampus.bootcamp2026.ui.theme.Blue

@Composable
fun InvitesScreen(
    viewModel: InvitesViewModel = viewModel<InvitesViewModel>()
) {
    val state by viewModel.uiState.collectAsState()

    when(val currentState = state) {
        is InvitesState.Error -> {
            Box(Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = currentState.reason)
                    Button(
                        onClick = { viewModel.getData() },
                        modifier = Modifier.background(Blue)
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        Text("Обновить")
                    }
                }
            }
        }
        is InvitesState.Content -> {
            InvitesScreenContent(currentState)
        }
        is InvitesState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Blue)
            }
        }
    }

}

@Composable
fun InvitesScreenContent(
    state: InvitesState.Content
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.padding()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ваши приглашения",
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(top = 5.dp),
                textAlign = TextAlign.Left
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize().padding(horizontal = 10.dp).verticalScroll(scrollState)
                ) {
                    state.meeting.forEach { meeting  ->
                        InvitationCard(meeting.name,
                            meeting.startTime,
                            meeting.endTime)
                    }

                }
            }
        }
    }
}

