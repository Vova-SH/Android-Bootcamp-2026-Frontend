package ru.sicampus.bootcamp2026.data.dto.costilSerializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Time
import java.util.Date

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Time::class)
class TimeSerializer : KSerializer<Time> {

    override fun serialize(encoder: Encoder, value: Time) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Time {
        return Time.valueOf(decoder.decodeString())

    }
}