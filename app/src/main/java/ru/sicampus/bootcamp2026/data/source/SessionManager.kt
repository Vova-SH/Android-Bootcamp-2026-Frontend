package ru.sicampus.bootcamp2026.data.source

object SessionManager {
    var authHeader: String? = null
        private set
    var currentUserId: Long? = null
        private set

    fun saveSession(auth: String, userId: Long) {
        authHeader = auth
        currentUserId = userId
    }

    fun clear() {
        authHeader = null
        currentUserId = null
    }

    fun tryRestoreSession(): Boolean {
        val token = TokenStorage.accessToken
        val userId = TokenStorage.userId

        if (token != null && userId != null && userId != -1L) {
            authHeader = token
            currentUserId = userId
            return true
        }
        return false
    }

    fun isLoggedIn(): Boolean = authHeader != null
}