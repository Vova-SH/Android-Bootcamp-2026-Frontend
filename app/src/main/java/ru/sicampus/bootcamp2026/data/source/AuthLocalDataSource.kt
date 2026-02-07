package ru.sicampus.bootcamp2026.data.source

import kotlin.io.encoding.Base64


object AuthLocalDataSource {
    val token: String? get() = _cacheToken

    private var _cacheToken: String? = null

    fun setToken(login: String?, password: String): String?{
        val credentials = "$login:$password"
        val base64String = Base64.encode(credentials.toByteArray())
        _cacheToken = base64String
        return _cacheToken!!
    }

    fun clearToken() {
        _cacheToken = null
    }
}