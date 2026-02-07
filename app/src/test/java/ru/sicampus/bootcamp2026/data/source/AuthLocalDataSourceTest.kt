package ru.sicampus.bootcamp2026.data.source

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthLocalDataSourceTest {

    @Test
    fun `setToken should correctly encode login and password to Basic auth format`() {
        val login = "user"
        val password = "password"
        val expectedToken = "Basic dXNlcjpwYXNzd29yZA=="

        AuthLocalDataSource.setToken(login, password)
        
        assertEquals(expectedToken, AuthLocalDataSource.token)
    }

    @Test
    fun `clearToken should set token to null`() {
        AuthLocalDataSource.setToken("user", "pass")
        AuthLocalDataSource.clearToken()
        
        assertNull(AuthLocalDataSource.token)
    }
}
