package com.example.user_main.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comon.User

@Composable
fun ProfileContent(
    user: User,
    onEditClick: () -> Unit ={},
    onLogoutClick: () -> Unit ={}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("ФИО", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(user.fullName, style = MaterialTheme.typography.titleMedium)

                Spacer(
                    modifier = Modifier.height(10.dp),
                )
                Text("Телефон", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(user.phoneNumber, style = MaterialTheme.typography.bodyMedium)

                Spacer(
                    modifier = Modifier.height(10.dp),

                )

                Text("Департамент", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(user.department, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(150.dp))

        Button(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Редактировать")
        }

        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти")
        }
        Spacer(modifier = Modifier.weight(0.3f))

    }
}

