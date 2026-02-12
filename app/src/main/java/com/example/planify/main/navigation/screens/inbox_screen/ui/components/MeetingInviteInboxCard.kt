package com.example.planify.main.navigation.screens.inbox_screen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Clock
import com.adamglin.phosphoricons.regular.MapPin
import com.adamglin.phosphoricons.regular.User
import com.adamglin.phosphoricons.regular.Users
import com.example.planify.R
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.withShapeBackground
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

@Composable
fun MeetingInviteInboxCard(
    meetingInfo: MeetingContext,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = Locals.shapes.mediumShape
    val colors = MaterialTheme.colorScheme

    val meeting = meetingInfo.meeting

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Locals.dimens.meetingInboxCardHeight)
            .padding(horizontal = Locals.spacing.m)
            .clip(shape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .withShapeBackground(
                        color = colors.surface,
                        shape = shape
                    )
                    .padding(
                        horizontal = Locals.spacing.l,
                        vertical = Locals.spacing.m
                    ),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(Locals.spacing.xs)
            ) {
                Text(
                    text = meeting.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Locals.extras.foreground
                    )
                )
                Text(
                    text = meeting.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = Locals.extras.mutedForeground
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(Locals.spacing.xxxs))
                Column(
                    verticalArrangement = Arrangement.spacedBy(Locals.spacing.xs)
                ) {
                    InfoRow(
                        icon = PhosphorIcons.Regular.Clock,
                        text = meeting.startsAt
                            .format(DateTimeFormatter.ofPattern("HH:mm"))
                                + " - " + meeting.startsAt.plusHours(meeting.duration.toLong())
                            .format(DateTimeFormatter.ofPattern("HH:mm")),
                        iconColor = Locals.extras.primary,
                    )
                    InfoRow(
                        icon = PhosphorIcons.Regular.MapPin,
                        text = meeting.location,
                        iconColor = colors.secondary,
                    )
                    InfoRow(
                        icon = PhosphorIcons.Regular.User,
                        text = meetingInfo.participantProfiles.first { participant ->
                            participant.userId == meetingInfo.meeting.ownerId
                        }.let { "${it.firstName} ${it.lastName}" },
                        iconColor = Locals.extras.primary,
                    )
                    InfoRow(
                        icon = PhosphorIcons.Regular.Users,
                        text = meetingInfo.participantProfiles.size.toString(),
                        iconColor = Locals.extras.primary,
                    )
                }

                Spacer(modifier = Modifier.height(Locals.spacing.s))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Locals.dimens.buttonMeetingInboxCardHeight),
                    horizontalArrangement = Arrangement.spacedBy(Locals.spacing.s)
                ) {
                    Button(
                        modifier = Modifier
                            .background(
                                brush = Locals.gradients.blue,
                                shape = Locals.shapes.smallShape
                            )
                            .fillMaxHeight()
                            .weight(1f),
                        onClick = onAccept,
                        shape = Locals.shapes.smallShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.accept),
                            color = colors.onPrimary
                        )
                    }

                    Button(
                        modifier = Modifier
                            .border(
                                color = Locals.extras.border,
                                shape = Locals.shapes.smallShape,
                                width = 1.dp
                            )
                            .fillMaxHeight()
                            .weight(1f),
                        onClick = onReject,
                        shape = Locals.shapes.smallShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.surface
                        )

                    ) {
                        Text(
                            text = stringResource(R.string.decline),
                            color = colors.onSurface
                        )
                    }

                }

            }
        }

    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String,
    iconColor: Color
) {
    val colors = MaterialTheme.colorScheme
    val shape = Locals.shapes.smallShape

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Locals.spacing.m)
    ) {
        Box(
            modifier = Modifier
                .size(Locals.icons.smallPlus)
                .shadow(
                    elevation = 1.dp,
                    shape = shape
                )
        ) {
            Box(
                modifier = Modifier
                    .withShapeBackground(
                        color = colors.surface,
                        shape = shape
                    )
                    .border(
                        color = Locals.extras.border,
                        shape = shape,
                        width = 1.dp
                    )
                    .fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Locals.spacing.xxs),
                    tint = iconColor
                )
            }
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                color = Locals.extras.foreground,
                fontSize = 14.sp
            )
        )
    }
}