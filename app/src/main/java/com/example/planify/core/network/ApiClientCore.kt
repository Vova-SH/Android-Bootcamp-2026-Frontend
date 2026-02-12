package com.example.planify.core.network

import io.ktor.client.HttpClient

open class ApiClientCore(  // TODO: Do i need it?
    protected val httpClient: HttpClient
)
