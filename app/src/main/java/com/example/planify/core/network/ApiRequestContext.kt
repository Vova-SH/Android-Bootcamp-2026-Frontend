package com.example.planify.core.network

class ApiRequestContext private constructor(
    private val metadata: Map<String, Any> = HashMap()
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> getFromMeta(key: String): T? {
        return metadata[key] as T?
    }

    class Builder(
        private val metadata: MutableMap<String, Any> = HashMap()
    ) {
        companion object {
            fun empty(): ApiRequestContext = Builder().build()
        }

        fun <T : Any> meta(key: String, value: T): Builder {
            metadata[key] = value
            return this
        }

        fun build(): ApiRequestContext {
            return ApiRequestContext(metadata = metadata)
        }
    }
}
