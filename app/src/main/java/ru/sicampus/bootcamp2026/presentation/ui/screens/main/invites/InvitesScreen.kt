package ru.sicampus.bootcamp2026.presentation.ui.screens.main.invites

import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.presentation.ui.theme.AndroidBootcamp2026FrontendTheme

//@Preview(showSystemUi = true)
@Composable
fun InvitesScreen() {
    Text("invites")
}



@Composable
fun MeetInvite() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(18.dp))
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp),
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
            ) {
                Text(
                    "Планирование тестирования",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text("9:20 5 апр.", style = MaterialTheme.typography.labelMedium)
                Text("Иван Лебедев", style = MaterialTheme.typography.labelMedium)
            }
            Spacer(modifier = Modifier.weight(0.1f))

            Row(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ReactButton(Icons.Default.Check) { }
                ReactButton(Icons.Default.Close) { }
            }
        }
    }
}

@Composable
fun ReactButton(
    img: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.size(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(img, contentDescription = null)
        }
    }
}



@Preview
@Composable
private fun MeetInvitePreview() {
    AndroidBootcamp2026FrontendTheme(darkTheme = true) {
        MeetInvite()
    }
}
