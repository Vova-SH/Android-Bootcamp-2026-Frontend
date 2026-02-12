package com.example.planify.core.entities

import io.ktor.client.statement.HttpResponse

data class ApiCallResult<T>(
    val httpResponse: HttpResponse,
    val data: T?
)
