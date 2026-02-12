package com.example.planify.main.navigation.screens.create_meeting_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.CaretLeft
import com.example.planify.R
import com.example.planify.core.ui.dialogs.AlertDialog
import com.example.planify.core.ui.pager_router_screen.PagerRouterNavigator
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.withShapeBackground
import com.example.planify.main.navigation.screens.create_meeting_screen.CreateMeetingRoute
import com.example.planify.main.navigation.screens.create_meeting_screen.CreateMeetingViewModel

@Composable
fun BottomBar(
    navigator: PagerRouterNavigator,
    viewModel: CreateMeetingViewModel
) {
    val colors = MaterialTheme.colorScheme

    val showDialog by viewModel.showDialog.collectAsState()

    val draftState by viewModel.meetingDraftState.collectAsState()

    if (showDialog) {
        AlertDialog(
            title = stringResource(R.string.cant_create),
            message = stringResource(R.string.cant_create_placeholder),
            onDismiss = { viewModel.closeDialog() },
            onCancel = { viewModel.closeDialog() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(Locals.dimens.createMeetingBottomBarHeight)
            .background(colors.surface),
        verticalArrangement = Arrangement.spacedBy(Locals.spacing.xxs),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .wrapContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Locals.spacing.m,
                        vertical = Locals.spacing.s
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${navigator.currentRouteIndex + 1}/3",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurface
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = when (navigator.currentRouteIndex) {
                        0 -> {
                            stringResource(R.string.title_info)
                        }

                        1 -> {
                            stringResource(R.string.title_time)
                        }

                        2 -> {
                            stringResource(R.string.title_participants)
                        }

                        else -> {
                            ""
                        }
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurface
                )
            }

            when (navigator.currentRouteIndex) {
                0 -> {
                    BottomButton(
                        text = "Далее",
                        onClick = {
                            navigator.navigateTo(CreateMeetingRoute.Time)
                        },
                        buttonClickable = {
                            draftState.name != null &&
                            draftState.description != null &&
                            draftState.location != null
                        }
                    )
                }

                1 -> {
                    RowWith2Buttons(
                        mainButtonTitle = "Далее",
                        onBackButton = {
                            navigator.navigateTo(CreateMeetingRoute.Info)
                        },
                        onButtonClick = {
                            navigator.navigateTo(CreateMeetingRoute.Participants)
                        },
                        buttonClickable = {
                            draftState.selectedTimeSlots.isNotEmpty()
                        }
                    )
                }

                2 -> {
                    RowWith2Buttons(
                        mainButtonTitle = "Создать",
                        onBackButton = {
                            navigator.navigateTo(CreateMeetingRoute.Time)
                        },
                        onButtonClick = {
                            viewModel.runCreateMeeting()
                        },
                        buttonClickable = {
                            viewModel.canCreate()
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun RowWith2Buttons(
    mainButtonTitle: String,
    onBackButton: () -> Unit,
    onButtonClick: () -> Unit,
    buttonClickable: () -> Boolean
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                paddingValues = PaddingValues(
                    start = Locals.spacing.m
                )
            )
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .withShapeBackground(
                    shape = CircleShape,
                    color = colors.surface
                )
                .clickable(
                    onClick = onBackButton
                )
        ) {
            Icon(
                modifier = Modifier
                    .size(Locals.icons.medium)
                    .padding(Locals.spacing.xs),
                imageVector = PhosphorIcons.Regular.CaretLeft,
                contentDescription = null
            )
        }

        BottomButton(
            text = mainButtonTitle,
            onClick = onButtonClick,
            buttonClickable = buttonClickable
        )
    }
}


@Composable
fun BottomButton(
    text: String,
    onClick: () -> Unit,
    buttonClickable: () -> Boolean
) {
    val shape = Locals.shapes.mediumShape
    val colors = MaterialTheme.colorScheme

    val clickable = buttonClickable()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Locals.spacing.m
            )
            .alpha(if (clickable) 1f else 0.7f)
            .height(Locals.dimens.createMeetingBottomButtonHeight),
        shape = shape
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Locals.gradients.blue,
                    shape = shape
                )
                .clickable(
                    onClick = onClick,
                    enabled = clickable
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(Locals.spacing.m),
                text = text,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = colors.onPrimary
            )
        }
    }
}