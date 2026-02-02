package ru.sicampus.bootcamp2026.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.components.InvitationCard

@Composable
fun InvitesScreen() {
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
                fontFamily = FontFamily(androidx.
                compose.ui.text.font.Font(R.font.montserrat_semibold)),
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    item {
                        InvitationCard("")
                        InvitationCard("")
                        InvitationCard("")
                        InvitationCard("")
                        InvitationCard("")
                        InvitationCard("")
                        InvitationCard("")
                        InvitationCard("")
                        InvitationCard("")
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun View() {
    InvitesScreen()
}