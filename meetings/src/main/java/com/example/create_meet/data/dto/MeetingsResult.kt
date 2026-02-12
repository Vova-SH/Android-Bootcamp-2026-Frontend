package com.example.create_meet.data.dto

sealed class MeetingsResult {
    data class Success(val meetings: List<MeetingResponse>) : MeetingsResult()
    data class Error(val message: String) : MeetingsResult()
}