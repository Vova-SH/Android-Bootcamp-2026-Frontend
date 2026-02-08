package ru.sicampus.bootcamp2026.ui.screen.users

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.ui.components.*
import ru.sicampus.bootcamp2026.ui.theme.*

@Composable
fun UsersListScreen(
    viewModel: UsersListViewModel,
    onUserClick: (Long) -> Unit
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    JuicyBackground {
        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Люди",
                        style = MaterialTheme.typography.displayMedium
                    )

                    Surface(
                        onClick = {
                            searchQuery = ""
                            viewModel.loadUsers()
                        },
                        shape = CircleShape,
                        color = SurfaceWhite,
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Refresh, null, tint = BrandPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                JuicyTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.onSearchQueryChanged(it)
                    },
                    label = "Найти...",
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Rounded.Search, null, tint = TextSecondary) }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = BrandPrimary
                    )
                } else if (users.isEmpty()) {
                    Text(
                        "Пользователи не найдены",
                        modifier = Modifier.align(Alignment.Center),
                        color = TextTertiary
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(users, key = { it.id }) { user ->
                            JuicyUserRow(
                                user = user,
                                onClick = { onUserClick(user.id) },
                                onCopy = {
                                    clipboardManager.setText(AnnotatedString(user.email))
                                    Toast.makeText(context, "Email скопирован", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JuicyUserRow(user: UserDto, onClick: () -> Unit, onCopy: () -> Unit) {
    JuicyCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(SecondaryGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.firstName.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${user.firstName} ${user.secondName}",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                if (!user.position.isNullOrBlank()) {
                    Text(
                        text = user.position,
                        style = MaterialTheme.typography.bodySmall,
                        color = BrandPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Email,
                        null,
                        modifier = Modifier.size(12.dp),
                        tint = TextTertiary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            IconButton(
                onClick = onCopy,
                modifier = Modifier
                    .size(36.dp)
                    .background(SurfaceLight, RoundedCornerShape(10.dp))
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Copy Email",
                    modifier = Modifier.size(18.dp),
                    tint = TextSecondary
                )
            }
        }
    }
}