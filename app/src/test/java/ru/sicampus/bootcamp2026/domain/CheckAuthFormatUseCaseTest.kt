package ru.sicampus.bootcamp2026.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckAuthFormatUseCaseTest {

    private val useCase = CheckAuthFormatUseCase()

    @Test
    fun `invoke should return true for valid login and password`() {
        assertTrue(useCase("user123", "password123"))
    }

    @Test
    fun `invoke should return false for short login`() {
        assertFalse(useCase("us", "password"))
    }

    @Test
    fun `invoke should return false for login with special characters`() {
        assertFalse(useCase("user@123", "password"))
    }

    @Test
    fun `invoke should return false for empty password`() {
        assertFalse(useCase("user123", ""))
    }

    @Test
    fun `invoke should return false for blank password`() {
        assertFalse(useCase("user123", "   "))
    }
}
