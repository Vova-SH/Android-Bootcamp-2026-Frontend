package com.example.planify.core.network.middleware

import com.example.planify.core.network.ApiRequestContext

interface ApiClientMiddleware<In, Out> {
    suspend fun proceed(
        context: ApiRequestContext,
        next: suspend (In) -> Any?,
        input: In
    ): Out
}
