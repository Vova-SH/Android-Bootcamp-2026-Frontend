package com.example.planify.main.navigation.screens.create_meeting_screen.ui.steps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.planify.R
import com.example.planify.core.ui.state.ResourceState
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.features.profiles.domain.entities.Profile
import com.example.planify.main.navigation.screens.create_meeting_screen.CreateMeetingViewModel

@Composable
fun ParticipantRow(
    profile: Profile,
    checked: Boolean,
    onToggle: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shape = Locals.shapes.mediumShape

    val containerColor = if (checked)
        colors.surfaceVariant.copy(alpha = 0.45f)
    else
        colors.surface

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(Locals.dimens.createMeetingParticipantCardHeight)
            .clip(shape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onToggle() },
        color = containerColor,
        shape = shape,
        border = BorderStroke(1.dp, Locals.extras.border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Locals.spacing.s),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { onToggle() }
            )

            Spacer(modifier = Modifier.width(Locals.spacing.xs))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(Locals.icons.medium)
                    .background(colors.surfaceVariant.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = profile.profileImageUrl,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(Locals.spacing.s))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "${profile.firstName} ${profile.lastName}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (checked) FontWeight.SemiBold else FontWeight.Medium,
                    color = colors.onBackground
                )
                Text(
                    text = profile.department,
                    style = MaterialTheme.typography.bodySmall,
                    color = Locals.extras.mutedForeground.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@Composable
private fun Chip(
    text: String,
    onRemove: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .padding(vertical = Locals.spacing.xxxs),
        shape = Locals.shapes.mediumShape,
        color = colors.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(
            color = Locals.extras.border.copy(alpha = 0.5f),
            width = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = Locals.spacing.xs,
                    vertical = Locals.spacing.xxs
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(Locals.spacing.xs))
            Text(
                text = "×",
                style = MaterialTheme.typography.labelMedium,
                fontSize = 18.sp,
                color = Locals.extras.mutedForeground,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onRemove() }
                    .padding(horizontal = Locals.spacing.xxs)
            )
        }
    }
}

@Composable
fun ChipRow(
    profiles: List<Profile>,
    onRemove: (Profile) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Locals.spacing.xs)
    ) {
        items(profiles) { profile ->
            Chip(
                text = "${profile.firstName} ${profile.lastName}",
                onRemove = { onRemove(profile) }
            )
        }
    }
}

@Composable
fun CreateMeetingStep3(
    viewModel: CreateMeetingViewModel,
) {
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()

    val draftState by viewModel.meetingDraftState.collectAsState()
    val searchState by viewModel.profilesSearchState.collectAsState()
    val searchResultState by viewModel.profilesSearchResultState.collectAsState()

    var chipRowState by remember { mutableStateOf( listOf<Profile>() ) }

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItems = listState.layoutInfo.totalItemsCount

            lastVisibleItem != null && lastVisibleItem >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) viewModel.fetchProfiles()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    top = Locals.spacing.m,
                    start = Locals.spacing.m,
                    end = Locals.spacing.m,
                )
            )
    ) {
        Text(
            text = stringResource(R.string.step3_inviting),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(Locals.spacing.s))

        Text(
            text = stringResource(R.string.step3_chosen) + " ${draftState.inviteUsersIds.size}",
            style = MaterialTheme.typography.bodySmall,
            color = Locals.extras.mutedForeground.copy(alpha = 0.75f)
        )

        Spacer(modifier = Modifier.height(Locals.spacing.s))

        when (searchResultState) {
            is ResourceState.Error -> {}
            is ResourceState.Idle -> {}
            is ResourceState.Loading, is ResourceState.Refreshing -> {}
            is ResourceState.Success<Map<Long, Profile>> -> {
                val state = searchResultState as ResourceState.Success<Map<Long, Profile>>

                ChipRow(
                    profiles = chipRowState,  //state.data.values.filter { draftState.inviteUsersIds.contains(it.userId) },
                    onRemove = { profile ->
                        viewModel.removeInvite(profile.userId)
                        chipRowState -= profile
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(Locals.spacing.s))

        OutlinedTextField(
            value = searchState.query,
            onValueChange = { viewModel.setProfilesQuery(it) },
            singleLine = true,
            placeholder = { Text(stringResource(R.string.step3_search_placeholder)) },
            shape = Locals.shapes.mediumShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Locals.extras.border,
                unfocusedBorderColor = Locals.extras.border,
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.surface,
                cursorColor = colors.primary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Locals.spacing.s))

        Divider(color = Locals.extras.border)

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(Locals.spacing.s),
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(0.dp))
            }

            when(searchResultState) {
                is ResourceState.Error -> {}
                is ResourceState.Idle -> {}
                is ResourceState.Loading, is ResourceState.Refreshing -> {}
                is ResourceState.Success<*> -> {
                    val state = searchResultState as ResourceState.Success<Map<Long, Profile>>

                    items(state.data.values.toList(), key = { it.userId }) { profile ->
                        val checked = draftState.inviteUsersIds.contains(profile.userId)
                        ParticipantRow(
                            profile = profile,
                            checked = checked
                        ) {
                            viewModel.toggleInvite(profile.userId)
                            if (checked) {
                                chipRowState -= profile
                            } else {
                                chipRowState += profile
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
}
