package ru.sicampus.bootcamp2026.data.mappers

import ru.sicampus.bootcamp2026.data.dto.MeetDto
import ru.sicampus.bootcamp2026.data.dto.MeetTimeSlotDto
import ru.sicampus.bootcamp2026.domain.models.meet.Meet
import ru.sicampus.bootcamp2026.domain.models.meet.MeetTimeSlot
import java.time.Instant
import java.time.LocalDate

fun MeetDto.toDomain(): Meet? {
    return Meet(
        id = id ?: return null,
        organizerId = organizerId ?: return null,
        title = title ?: return null,
        description = description ?: return null,
        timeSlot = timeSlot?.toDomain() ?: return null,
        membersIds = membersIds ?: return null,
        invitedIds = invitedIds ?: return null,
        createdAt = Instant.parse(createdAt) ?: return null,
    )
}

fun Meet.toDto(): MeetDto = MeetDto(
    id = id,
    organizerId = organizerId,
    title = title,
    description = description,
    timeSlot = timeSlot.toDto(),
    membersIds = membersIds,
    invitedIds = invitedIds,
    createdAt = createdAt.toString()
)

fun MeetTimeSlotDto.toDomain(): MeetTimeSlot? {
    return MeetTimeSlot(
        date = LocalDate.parse(date),
        startHour = startHour ?: return null,
        durationHours = durationHours ?: return null,
    )
}

fun MeetTimeSlot.toDto(): MeetTimeSlotDto = MeetTimeSlotDto(
    date = date.toString(),
    startHour = startHour,
    durationHours = durationHours
)