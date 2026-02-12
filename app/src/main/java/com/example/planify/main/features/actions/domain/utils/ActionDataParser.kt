package com.example.planify.main.features.actions.domain.utils

import com.example.planify.core.data.serializers.jsonCore
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer

class ActionDataParser {
    private val serializers: MutableMap<String, KSerializer<*>> = HashMap()

    fun register(type: String, serializer: KSerializer<*>) {
        serializers[type] = serializer
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getSerializer(type: String): KSerializer<T> {
        return serializers[type]!! as KSerializer<T>
    }

    fun deserialize(data: JsonElement, type: String): Any {
        return jsonCore.decodeFromJsonElement(getSerializer(type), data)
    }

    fun deserialize(data: String, type: String): Any {
        return jsonCore.decodeFromString(getSerializer(type), data)
    }

    fun serializeJsonElement(data: JsonElement): String {
        return jsonCore.encodeToString(data)
    }
}

inline fun <reified T> ActionDataParser.registerSchema(type: String) {
    register(type, serializer<T>())
}
