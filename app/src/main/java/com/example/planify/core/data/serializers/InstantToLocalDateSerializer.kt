package com.example.planify.core.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


object InstantToLocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateFromInstant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val instant = value.atStartOfDay(ZoneId.of("UTC")).toInstant()
        encoder.encodeString(instant.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val instant = Instant.parse(decoder.decodeString())
        return instant.atZone(ZoneId.systemDefault()).toLocalDate()
    }
}
