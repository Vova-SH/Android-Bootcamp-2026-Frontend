package ru.sicampus.bootcamp2026.data.source

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object AuthLocalDataSource {
    @Volatile
    private var _cacheToken: String? = null

    val token: String? get() = _cacheToken

    @OptIn(ExperimentalEncodingApi::class)
    fun setToken(email: String, password: String) {
        val decodePhrase = "$email:$password"
        _cacheToken = "Basic ${Base64.encode(decodePhrase.toByteArray())}"
    }

    fun clearToken() {
        _cacheToken = null
    }
}