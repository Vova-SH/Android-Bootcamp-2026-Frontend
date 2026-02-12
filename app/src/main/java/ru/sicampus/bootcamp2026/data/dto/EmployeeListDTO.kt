package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.sicampus.bootcamp2026.domain.entities.EmployeeListEntity

@Serializable
data class EmployeeListDTO (
    @SerialName("name")
    val name: String?,
    @SerialName("username")
    val username: String?,
    @SerialName("photoUrl")
    val photoUrl: String?,
)
fun EmployeeListDTO.toEntity() : EmployeeListEntity{
    return EmployeeListEntity(
        name = name.orEmpty(),
        username = requireNotNull(username){"username is null"},
        photoUrl = photoUrl ?: "https://bootcamp-back.indx0.ru:8000/images/pfp1.jpg"
    )
}
