package com.example.planify.main.navigation.screens.create_meeting_screen.components.create_meeting_floating_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Calendar
import com.adamglin.phosphoricons.regular.Clock
import com.adamglin.phosphoricons.regular.Lightning
import com.adamglin.phosphoricons.regular.X
import com.example.planify.R
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.common.ui.withShapeBackground

@Composable
fun CreateMeetingDialog(
    onClose: () -> Unit,
    onCreateClick: () -> Unit
) {

    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    paddingValues = PaddingValues(top = Locals.spacing.xxs)
                )
                .background(color = colors.background),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Locals.spacing.m),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .background(color = colors.background),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                                paddingValues = PaddingValues(
                                    top = Locals.spacing.xxs
                                )
                            ),
                        text = stringResource(R.string.create_meeting_title),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(Locals.spacing.xxs))

                    Text(
                        text = stringResource(R.string.create_meeting_title_desc),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(Locals.icons.smallPlus)
                        .clickable(
                            onClick = onClose
                        ),
                    imageVector = PhosphorIcons.Regular.X,
                    contentDescription = null
                )
            }

            TypeOfMeeting(
                icon = PhosphorIcons.Regular.Calendar,
                iconColor = Color.Blue,
                title = stringResource(R.string.create_meeting),
                description = stringResource(R.string.create_meeting_desc),
                onClick = onCreateClick
            )
            TypeOfMeeting(
                icon = PhosphorIcons.Regular.Lightning,
                iconColor = colors.tertiary,
                title = stringResource(R.string.fast_meeting),
                description = stringResource(R.string.fast_meeting_desc),
                onClick = {}
            )
            TypeOfMeeting(
                icon = PhosphorIcons.Regular.Clock,
                iconColor = Color.Green,
                title = stringResource(R.string.book_slot),
                description = stringResource(R.string.book_slot_desc),
                onClick = {}
            )
        }
    }
}

@Composable
fun TypeOfMeeting(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val shape = Locals.shapes.mediumShape
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Locals.spacing.m,
                vertical = Locals.spacing.xxs
            )
            .clip(shape = shape)
            .border(
                color = Locals.extras.border,
                shape = shape,
                width = 1.dp
            )
            .height(Locals.dimens.typeOfMeetingCardHeight)
            .clickable(
                onClick = onClick
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = colors.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Locals.spacing.m)
                .background(color = colors.background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Locals.spacing.m)
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .withShapeBackground(
                        shape = CircleShape,
                        color = iconColor.copy(
                            alpha = 0.1f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(Locals.icons.mediumLower)
                        .padding(Locals.spacing.xs),
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(Locals.spacing.s),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Locals.extras.mutedForeground.copy(
                        alpha = 0.6f
                    )
                )
            }
        }
    }

}

