package ru.sicampus.bootcamp2026.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.sql.Timestamp


@Serializable
data class BookingsDto(
    /**
     * Тебе
     */
    @SerialName("yours")
    val you : List<BookingDto>? = null,
    /**
     * Твои
     */
    @SerialName("you")
    val yours : List<BookingDto>? = null
)


/**
 * Встреча (Meeting)
 */
@Serializable
data class BookingDto (
    @SerialName("Booking")
    val name: String? = null,
    /**
     * ФИО + почта
     */
    @SerialName("Employee")
    val employeeAdmin: String? = null,
    @SerialName("start_booking")
    val startBooking: String? = null,
    @SerialName("end_booking")
    val endBooking : String? = null,
    @SerialName("invited")
    val invited : List<String>? = null,
    @SerialName("Approval")
    val approval: Boolean? = null,
)