package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SpringPageDto<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val last: Boolean,
    val size: Int,
    val number: Int,
    val numberOfElements: Int? = null,
    val first: Boolean? = null,
    val empty: Boolean? = null
)