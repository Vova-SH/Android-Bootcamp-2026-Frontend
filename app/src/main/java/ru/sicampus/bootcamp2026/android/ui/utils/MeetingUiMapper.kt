package ru.sicampus.bootcamp2026.android.ui.utils

import ru.sicampus.bootcamp2026.android.data.dto.MeetingResponse
import ru.sicampus.bootcamp2026.android.ui.components.MeetingUi

fun MeetingResponse.toMeetingUi( organizerName: String, myUserId: Long?): MeetingUi {
    return MeetingUi(
        organizerName = organizerName,
        title = title,
        description = description,
        dateText = formatDateDdMmYyyy(startAt),
        timeText = formatTimeRange(startAt, endAt),
        isMyMeeting = myUserId == organizerId
    )
}
