package ru.sicampus.bootcamp2026.domain.model

/**
 * Модель пагинации
 */
data class PaginatedData<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val number: Int,
    val size: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)

