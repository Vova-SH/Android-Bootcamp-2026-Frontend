package ru.sicampus.bootcamp2026.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeesDto(
    @SerialName("employees")
    val employees: List<EmployeeDto>? = null
)

/**
 * Пользователь (User)
 */
@Serializable
data class EmployeeDto(
//    @SerialName("id")
//    val id: Long? = null,
    @SerialName("last_name")
    val surname: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("father_name")
    val patronymic: String? = null,
    @SerialName("avatar")
    val avatar: String? = null,
    @SerialName("mail")
    val mail: String? = null,
    @SerialName("contacts")
    val contacts: List<Map<String, String>>? = null,
    @SerialName("age")
    val age: Int? = null,
) {

    /**
     * Вспомогательный класс для "избранных"
     */
    @Serializable
    data class FavoriteDto(
        @SerialName("id")
        val id: Long? = null,
        /**
         * У кого в избранных
         */
        @SerialName("Employee_id")
        val employeeId: Long? = null,
        /**
         * Кто в избранных
         */
        @SerialName("Employee_user")
        val employeeUser: Long? = null,
    )
}