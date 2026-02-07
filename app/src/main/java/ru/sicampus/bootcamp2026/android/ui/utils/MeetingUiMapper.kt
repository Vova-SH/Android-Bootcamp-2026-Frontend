package ru.sicampus.bootcamp2026.android.ui.mappers

import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import ru.sicampus.bootcamp2026.android.ui.components.MeetingUi
import ru.sicampus.bootcamp2026.android.ui.utils.formatDateDdMmYyyy
import ru.sicampus.bootcamp2026.android.ui.utils.formatTimeRange

fun MeetingResponse.toMeetingUi(organizerName: String): MeetingUi {
    return MeetingUi(
        organizerName = organizerName,
        title = title,
        description = description,
        dateText = formatDateDdMmYyyy(startAt),
        timeText = formatTimeRange(startAt, endAt)
    )
}