package ru.sicampus.bootcamp2026.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Обобщенный DTO для пагинированных данных
 */
@Serializable
data class PageResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val number: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)

