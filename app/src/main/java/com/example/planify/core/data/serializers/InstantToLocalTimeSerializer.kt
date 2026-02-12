package com.example.planify.core.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

object InstantToLocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalTimeFromInstant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        val instant = value.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()
        encoder.encodeString(instant.toString())
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        val instant = Instant.parse(decoder.decodeString())
        return instant.atZone(ZoneId.systemDefault()).toLocalTime()
    }
}
