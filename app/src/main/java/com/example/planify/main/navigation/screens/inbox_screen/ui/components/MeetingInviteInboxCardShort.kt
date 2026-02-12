package com.example.planify.main.navigation.screens.inbox_screen.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.CalendarBlank
import com.adamglin.phosphoricons.regular.Clock
import com.adamglin.phosphoricons.regular.MapPin
import com.adamglin.phosphoricons.regular.Pen
import com.example.planify.R
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.withShapeBackground
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import com.example.planify.main.features.meetings.domain.entities.MeetingInviteStatus
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MeetingInviteInboxCardShort(
    meetingInfo: MeetingContext,
    modifier: Modifier = Modifier
) {
    val shape = Locals.shapes.mediumShape
    val colors = MaterialTheme.colorScheme

    val meeting = meetingInfo.meeting

    val formatter1 = DateTimeFormatter.ofPattern("dd.MM", Locale("ru"))
    val formatter2 = DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Locals.dimens.meetingInboxCardHeightShort)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = meeting.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Locals.extras.foreground
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .border(
                                color = colors.primary.copy(0.6f),
                                shape = Locals.shapes.mediumShape,
                                width = 1.dp
                            )
                            .withShapeBackground(
                                color = colors.primaryContainer,
                                shape = Locals.shapes.mediumShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(Locals.spacing.xxs),
                            text = stringResource(R.string.sent_one),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.secondary
                        )
                    }

                }

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(Locals.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(Locals.icons.smallUp),
                        imageVector = PhosphorIcons.Regular.CalendarBlank,
                        contentDescription = null
                    )

                    Text(
                        text = meetingInfo.meeting.startsAt.format(formatter1),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Locals.extras.mutedForeground
                    )

                    Text(
                        text = "•"
                    )

                    Icon(
                        modifier = Modifier
                            .size(Locals.icons.smallUp),
                        imageVector = PhosphorIcons.Regular.Clock,
                        contentDescription = null
                    )

                    Text(
                        text = meetingInfo.meeting.startsAt.format(formatter2),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Locals.extras.mutedForeground
                    )
                }

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(Locals.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(Locals.icons.smallUp),
                        imageVector = PhosphorIcons.Regular.MapPin,
                        contentDescription = null
                    )

                    Text(
                        text = meetingInfo.meeting.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Locals.extras.mutedForeground
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        var count = 0

                        meetingInfo.participantProfiles.forEach { participant ->
                            if (count > 2) return@forEach
                            SmallPhotoIcon(
                                modifier = Modifier
                                    .size(Locals.icons.mediumLower)
                                    .offset(x = (-(Locals.icons.mediumLower/4))*count),
                                photoUrl = participant.profileImageUrl
                            )
                            count += 1
                        }

                        var accepted = 0

                        meetingInfo.invites.forEach { invite ->
                            if (invite.status == MeetingInviteStatus.ACCEPTED)
                                accepted += 1
                        }

                        Text(
                            modifier = Modifier
                                .offset(x = -(Locals.icons.mediumLower/4)),
                            text = "${accepted}/${meetingInfo.invites.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Locals.extras.mutedForeground
                        )
                    }


                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .size(Locals.icons.mediumLower)
                            .clip(CircleShape)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(Locals.icons.smallPlus),
                            imageVector = PhosphorIcons.Regular.Pen,
                            contentDescription = null
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun SmallPhotoIcon(
    modifier: Modifier = Modifier,
    photoUrl: String
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = photoUrl,
            contentDescription = null
        )
    }
}