package ru.sicampus.bootcamp2026.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.data.model.MeetingDto
import ru.sicampus.bootcamp2026.ui.theme.BackgroundColor
import ru.sicampus.bootcamp2026.ui.theme.PrimaryPurple
import ru.sicampus.bootcamp2026.ui.utils.formatUtcToLocal
import ru.sicampus.bootcamp2026.ui.viewmodel.InvitesViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitesScreen(
    onBackClicked: () -> Unit,
    viewModel: InvitesViewModel = viewModel()
) {
    val invites by viewModel.invites.collectAsState()

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Приглашения", fontWeight = FontWeight.SemiBold, fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            if (invites.isEmpty()) {
                item {
                    Text("Нет новых приглашений", color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }

            items(invites) { invite ->
                InviteItem(
                    invite = invite,
                    onAccept = { viewModel.answer(invite.id, true) },
                    onReject = { viewModel.answer(invite.id, false) }
                )
            }
        }
    }
}

@Composable
fun InviteItem(invite: MeetingDto, onAccept: () -> Unit, onReject: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(invite.title?.take(1) ?: "M", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = invite.title ?: "Без названия",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))

                val timeStr = formatUtcToLocal(invite.startsAt, "dd.MM HH:mm")

                Text(text = timeStr, fontSize = 14.sp, color = Color.Gray)
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onReject,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6), contentColor = Color.Black),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) { Text("Отклонить", fontWeight = FontWeight.SemiBold) }

            Button(
                onClick = onAccept,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple, contentColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) { Text("Принять", fontWeight = FontWeight.SemiBold) }
        }
    }
}