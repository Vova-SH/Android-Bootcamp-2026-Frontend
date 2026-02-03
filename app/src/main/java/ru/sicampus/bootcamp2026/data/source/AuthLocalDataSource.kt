package ru.sicampus.bootcamp2026.data.source

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object AuthLocalDataSource {
    val token: String? get() = "TAKE ME"

    private var _cashToken: String? = null

    @OptIn(ExperimentalEncodingApi::class)
    fun setToken(login: String, password: String) {
        val decodePhrase = "$login:$password"
        _cashToken = "Basic ${Base64.encode(decodePhrase.toByteArray())}"
    }

    fun clearToken() {

    }
}