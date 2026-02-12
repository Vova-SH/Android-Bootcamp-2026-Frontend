package ru.sicampus.bootcamp2026.data.dto.costilSerializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Date::class)
class DateSerializer : KSerializer<Date> {

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.toCertainFormat())
    }

    //deprecated logic )
    private fun Date.toCertainFormat(): String {
        return "${this.year}-${this.month}-${this.day}"
    }

    private fun parseFromCertainFormat(str: String): Date {
        return Date(
            //2022-02-22
            str.slice(0..3).toInt(),
            str.slice(5..6).toInt(),
            str.slice(8..9).toInt()
        )
    }

    override fun deserialize(decoder: Decoder): Date {
        return parseFromCertainFormat(decoder.decodeString())
    }

    //временная логика

}