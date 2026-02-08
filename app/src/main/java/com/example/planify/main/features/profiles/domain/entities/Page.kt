package com.example.planify.main.features.profiles.domain.entities

data class Page(
    val content: List<Profile> = emptyList(),  // TODO: Make generic
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val number: Int = 0,
    val size: Int = 0,
    val first: Boolean = false,
    val last: Boolean = false,
    val numberOfElements: Int = 0,
    val empty: Boolean = true
)