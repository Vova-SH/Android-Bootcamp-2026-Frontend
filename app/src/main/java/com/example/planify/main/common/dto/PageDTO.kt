package com.example.planify.main.common.dto

import com.example.planify.main.features.profiles.domain.entities.Page
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageDTO (
    @SerialName("content")
    val content: List<ProfileDTO> = emptyList(),

    @SerialName("totalElements")
    val totalElements: Long = 0,

    @SerialName("totalPages")
    val totalPages: Int = 0,

    @SerialName("number")
    val number: Int = 0,

    @SerialName("size")
    val size: Int = 0,

    @SerialName("first")
    val first: Boolean = false,

    @SerialName("last")
    val last: Boolean = false,

    @SerialName("numberOfElements")
    val numberOfElements: Int = 0,

    @SerialName("empty")
    val empty: Boolean = true
) {
    fun toEntity(): Page = Page(
        content = content.map { it.toEntity() },
        totalElements = totalElements,
        totalPages = totalPages,
        number = number,
        size = size,
        first = first,
        last = last,
        numberOfElements = numberOfElements,
        empty = empty
    )
}