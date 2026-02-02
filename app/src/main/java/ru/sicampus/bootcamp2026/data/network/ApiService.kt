package ru.sicampus.bootcamp2026.data.network

import retrofit2.http.GET
import ru.sicampus.bootcamp2026.data.model.MeetingDto

interface ApiService {
    @GET("meetings")
    suspend fun getMeetings(): List<MeetingDto>
}