package com.teto.planner.domain.model.common

data class PageMeta(
    val page: Int,
    val size: Int,
    val total: Long,
    val totalPages: Int = 0,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false
)