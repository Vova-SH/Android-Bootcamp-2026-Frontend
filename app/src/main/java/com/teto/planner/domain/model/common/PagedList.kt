package com.teto.planner.domain.model.common

data class PagedList<T>(
    val items: List<T>,
    val meta: PageMeta
)