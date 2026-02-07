package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PagingEmployeeListDTO(
    @SerialName("content")
    val content: List<EmployeeDTO>? = null,
    @SerialName("last")
    val last: Boolean? = null
)