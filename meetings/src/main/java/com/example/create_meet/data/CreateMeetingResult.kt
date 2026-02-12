package com.example.create_meet.data

import com.example.create_meet.data.dto.MeetingResponse

sealed class CreateMeetingResult {
    data class Success(val meeting: MeetingResponse) : CreateMeetingResult()
    data class Error(val message: String) : CreateMeetingResult()
}