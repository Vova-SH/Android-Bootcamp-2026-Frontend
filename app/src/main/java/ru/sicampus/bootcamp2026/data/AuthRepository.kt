package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource


class AuthRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authLocalDataSource: Unit,
) {
    suspend fun checkAndAuth(
        login: String,
        password: String,
    ): Result<Boolean> {
        /*authLocalDataSource.setToken(login, password)
        return authNetworkDataSource.chekAuth()*/

        return TODO("Provide the return value")
    }

}
