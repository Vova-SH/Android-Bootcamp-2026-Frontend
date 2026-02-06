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

    fun isLoggedIn(): Boolean = authHeader != null
}