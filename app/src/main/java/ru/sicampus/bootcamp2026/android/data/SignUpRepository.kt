package ru.sicampus.bootcamp2026.android.data

import ru.sicampus.bootcamp2026.android.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.android.data.source.SignUpNetworkDataSource

class SignUpRepository(
    private val signUpNetworkDataSource: SignUpNetworkDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) {
    suspend fun registerAndAuth(
        email: String,
        password: String,
        fullName: String,
        department: String,
        position: String,
    ): Result<Boolean> {
        return signUpNetworkDataSource.registerUser(
            email = email,
            password = password,
            fullName = fullName,
            department = department,
            position = position
        ).mapCatching { personResponse ->
            authLocalDataSource.setToken(email, password)
            authLocalDataSource.setUserId(personResponse.id)
            true
        }
    }
}