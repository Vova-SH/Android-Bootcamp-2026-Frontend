package com.example.planify.main.navigation.screens.settings_screen.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.X
import com.example.planify.R
import com.example.planify.main.common.entities.ThemeId
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.features.settings.domain.services.SettingsService
import com.example.planify.main.navigation.screens.fixed_screens.ErrorScreen
import com.example.planify.main.navigation.screens.init_screen.components.LoadingView
import com.example.planify.main.navigation.screens.settings_screen.SettingsViewModel
import com.example.planify.main.navigation.screens.settings_screen.UIState

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    SettingsScreen(
        viewModel = hiltViewModel(),
        onBack = onBack
    )
}

@Composable
fun SettingsInfo(
    setTheme: (ThemeId) -> Unit,
    setNotificationsEnable: (Boolean) -> Unit,
    uiState: UIState.ContentData,
    onBack: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val checkedStateTheme = remember { mutableStateOf(false) }
    val checkedStateNotifications = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = colors.primary
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(Locals.icons.smallPlus)
                        .clip(CircleShape)
                        .clickable(
                            onClick = onBack
                        )
                ) {
                    Icon(
                        imageVector = PhosphorIcons.Regular.X,
                        contentDescription = null
                    )
                }

            }

            Spacer(Modifier.height(12.dp))

            SettingsCard {
                SettingsSwitchRow(
                    title = stringResource(R.string.dark_theme),
                    subtitle = stringResource(R.string.dark_theme_desc),
                    checked = when(uiState.settings.theme) {
                        ThemeId.DARK -> true
                        ThemeId.LIGHT -> false
                        ThemeId.SYSTEM -> isSystemInDarkTheme()
                        null -> false
                    },
                    onCheckedChange = {
                        checkedStateTheme.value = it
                        if (checkedStateTheme.value) {
                            setTheme(ThemeId.DARK)
                        } else {
                            setTheme(ThemeId.LIGHT)
                        }
                    }
                )

                DividerLine()

                SettingsSwitchRow(
                    title = stringResource(R.string.notifications),
                    subtitle = stringResource(R.string.notifications_desc),
                    checked = uiState.settings.notifications ?: false,
                    onCheckedChange = {
                        checkedStateNotifications.value = it
                        if (checkedStateNotifications.value) {
                            setNotificationsEnable(true)
                        } else {
                            setNotificationsEnable(false)
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is UIState.Loading -> {
            LoadingView()
        }
        is UIState.ContentData -> {
            SettingsInfo(
                setTheme = { viewModel.setTheme(it) },
                setNotificationsEnable = { viewModel.setNotificationsEnable(it) },
                uiState = uiState as UIState.ContentData,
                onBack = onBack
            )
        }
        is UIState.Error -> {
            ErrorScreen((uiState as UIState.Error).message)
        }
    }

}

@Composable
private fun SettingsCard(
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colors.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 6.dp)
    ) {
        content()
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = colors.onBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = colors.primary.copy(alpha = 0.85f),
                checkedThumbColor = colors.onPrimary
            ),
            interactionSource = null
        )
    }
}

@Composable
private fun DividerLine() {
    val colors = MaterialTheme.colorScheme
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 14.dp)
            .border(1.dp, colors.outlineVariant)
    )
}