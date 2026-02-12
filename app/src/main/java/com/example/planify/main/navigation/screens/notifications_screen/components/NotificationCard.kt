package com.example.planify.main.navigation.screens.notifications_screen.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.CalendarBlank
import com.adamglin.phosphoricons.regular.CalendarCheck
import com.adamglin.phosphoricons.regular.Repeat
import com.adamglin.phosphoricons.regular.UserCheck
import com.example.planify.R
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.features.meetings.domain.entities.Meeting
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NotificationCard(
    modifier: Modifier = Modifier,
    type: NotificationType,
    firstName: String,
    lastName: String,
    meeting: Meeting
) {
    val colors = MaterialTheme.colorScheme

    val formatter = DateTimeFormatter.ofPattern("dd MMMM, HH:mm", Locale("ru"))

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = Locals.shapes.mediumShape,
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Locals.extras.border
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Locals.spacing.m),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(Locals.spacing.m)
            ) {
                when(type) {
                    NotificationType.INVITE_INCOMING -> {
                        Icon(
                            modifier = Modifier
                                .size(Locals.icons.smallPlus)
                                .align(Alignment.CenterVertically),
                            imageVector = PhosphorIcons.Regular.CalendarBlank,
                            contentDescription = null,
                            tint = colors.primary
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "$firstName $lastName " + stringResource(R.string.smn_invited_you_to_meeting) + " " + "\"" + meeting.name + "\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurface
                            )

                            Spacer(modifier = Modifier.height(Locals.spacing.xs))

                            Text(
                                text = meeting.startsAt.format(formatter),
                                style = MaterialTheme.typography.bodySmall,
                                color = Locals.extras.mutedForeground
                            )
                        }
                    }
                    NotificationType.INVITE_ACCEPTED -> {
                        Icon(
                            modifier = Modifier
                                .size(Locals.icons.smallPlus)
                                .align(Alignment.CenterVertically),
                            imageVector = PhosphorIcons.Regular.UserCheck,
                            contentDescription = null,
                            tint = Color.Green
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "$firstName $lastName " + stringResource(R.string.smn_accepted_your_invite) + " " + "\"" + meeting.name + "\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurface
                            )

                            Spacer(modifier = Modifier.height(Locals.spacing.xs))

                            Text(
                                text = meeting.startsAt.format(formatter),
                                style = MaterialTheme.typography.bodySmall,
                                color = Locals.extras.mutedForeground
                            )
                        }
                    }
                    NotificationType.MEETING_UPDATED -> {
                        Icon(
                            modifier = Modifier
                                .size(Locals.icons.smallPlus)
                                .align(Alignment.CenterVertically),
                            imageVector = PhosphorIcons.Regular.CalendarCheck,
                            contentDescription = null,
                            tint = colors.secondary
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.meeting) + " " + "\"" + meeting.name + "\"" + " " + stringResource(R.string.was_updated),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurface
                            )

                            Spacer(modifier = Modifier.height(Locals.spacing.xs))

                            Text(
                                text = meeting.startsAt.format(formatter),
                                style = MaterialTheme.typography.bodySmall,
                                color = Locals.extras.mutedForeground
                            )
                        }
                    }
                    NotificationType.RESCHEDULED_MEETING -> {
                        Icon(
                            modifier = Modifier
                                .size(Locals.icons.smallPlus)
                                .align(Alignment.CenterVertically),
                            imageVector = PhosphorIcons.Regular.Repeat,
                            contentDescription = null,
                            tint = colors.error
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "$firstName $lastName " + stringResource(R.string.suggest_reschedule_meeting) + " " + "\"" + meeting.name + "\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurface
                            )

                            Spacer(modifier = Modifier.height(Locals.spacing.xs))

                            Text(
                                text = meeting.startsAt.format(formatter),
                                style = MaterialTheme.typography.bodySmall,
                                color = Locals.extras.mutedForeground
                            )
                        }
                    }
                }
            }

        }
    }
}