package com.example.planify.main.navigation.screens.create_meeting_screen.ui.steps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.planify.R
import com.example.planify.core.ui.state.ResourceState
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.navigation.screens.create_meeting_screen.CreateMeetingViewModel
import java.time.LocalTime

@Composable
private fun TimeSlotItem(
    slot: Int,
    isBusy: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shape = Locals.shapes.mediumShape

    fun intToHourSlot(time: Int): String {
        return if (time < 24) "%02d:00".format(time)
            else "%02d:00".format(time - 24)
    }


    val borderColor = when {
        selected -> colors.primary
        isBusy -> Locals.extras.border
        else -> Locals.extras.border.copy(alpha = 0.5f)
    }

    val containerColor = when {
        isBusy -> colors.surfaceVariant.copy(alpha = 0.55f)
        else -> colors.surface
    }

    val textColor = when {
        selected -> colors.primary
        isBusy -> Locals.extras.mutedForeground.copy(alpha = 0.55f)
        else -> colors.onBackground
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(Locals.dimens.createMeetingTimeSlotHeight)
            .clip(shape)
            .background(containerColor)
            .clickable(
                enabled = !isBusy,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        color = containerColor,
        shape = shape,
        border = BorderStroke(
            width = if (selected) 1.6f.dp else 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Locals.spacing.s),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${intToHourSlot(slot)} - ${intToHourSlot(slot + 1)}",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )

            Spacer(Modifier.weight(1f))

            if (isBusy) {
                Text(
                    text = "Занято",
                    style = MaterialTheme.typography.labelSmall,
                    color = Locals.extras.mutedForeground.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Composable
fun CreateMeetingStep2(viewModel: CreateMeetingViewModel) {
    val colors = MaterialTheme.colorScheme

    val draftState by viewModel.meetingDraftState.collectAsState()
    val userSchedule by viewModel.userScheduleResourceState.collectAsState()

    LaunchedEffect(draftState.startsAtDate) {
        viewModel.fetchUserSchedule()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    top = Locals.spacing.m,
                    start = Locals.spacing.m,
                    end = Locals.spacing.m
                )
            )
    ) {
        Text(
            text = stringResource(R.string.step2_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Normal,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(Locals.spacing.xs))

        Text(
            text = stringResource(R.string.step2_desc),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = Locals.extras.mutedForeground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(Locals.spacing.s))

        Divider(color = Locals.extras.muted)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(
                    state = rememberScrollState()
                ),
            verticalArrangement = Arrangement.spacedBy(Locals.spacing.s),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(0.dp))

            when (userSchedule) {
                is ResourceState.Error -> {}
                is ResourceState.Idle -> {}
                is ResourceState.Loading, is ResourceState.Refreshing -> {}
                is ResourceState.Success<Map<Int, Boolean>> -> {
                    val state = userSchedule as ResourceState.Success<Map<Int, Boolean>>
                    fun isSelectable(i: Int) = state.data[i] == true

                    (0 until 24).forEach { slot ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            TimeSlotItem(
                                slot = slot,
                                isBusy = !isSelectable(slot),
                                selected = draftState.selectedTimeSlots.contains(slot),
                                onClick = {
                                    val selectedItems = draftState.selectedTimeSlots

                                    viewModel.setSelectedTimeSlots(
                                        when {
                                            selectedItems.size == 1 && slot > selectedItems.last() -> {
                                                val newSelection = mutableListOf<Int>()

                                                for (i in selectedItems.first()..slot) {
                                                    if (!isSelectable(i)) {
                                                        newSelection.clear()
                                                        continue
                                                    }

                                                    newSelection.add(i)
                                                }

                                                newSelection
                                            }
                                            else -> if (isSelectable(slot)) listOf(slot) else listOf()
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(0.dp))
        }
    }
}
