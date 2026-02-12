package com.example.planify.core.data.serializers

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val jsonCore = Json {
    serializersModule = SerializersModule {
        contextual(LocalDate::class, InstantToLocalDateSerializer)
        contextual(LocalDateTime::class, InstantToLocalDateTimeSerializer)
        contextual(LocalTime::class, InstantToLocalTimeSerializer)
    }
    ignoreUnknownKeys = true
}
