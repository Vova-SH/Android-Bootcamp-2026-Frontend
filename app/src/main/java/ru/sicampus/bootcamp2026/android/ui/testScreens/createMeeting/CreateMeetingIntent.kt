package ru.sicampus.bootcamp2026.android.ui.testScreens.createMeeting

sealed interface CreateMeetingIntent {
    data class UpdateTitle(val title: String) : CreateMeetingIntent
    data class UpdateDescription(val description: String) : CreateMeetingIntent
    data class UpdateDate(val date: String) : CreateMeetingIntent
    data class UpdateStartHour(val hour: String) : CreateMeetingIntent
    data class UpdateEndHour(val hour: String) : CreateMeetingIntent
    data class UpdateSearchQuery(val query: String) : CreateMeetingIntent
    data object SearchPerson : CreateMeetingIntent
    data class AddParticipant(val participant: Participant) : CreateMeetingIntent
    data class RemoveParticipant(val participantId: Long) : CreateMeetingIntent
    data object CreateMeeting : CreateMeetingIntent
}