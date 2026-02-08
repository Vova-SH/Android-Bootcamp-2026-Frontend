package ru.sicampus.bootcamp2026.data.network

import ru.sicampus.bootcamp2026.data.network.source.MeetingDataSource
import ru.sicampus.bootcamp2026.domain.entities.MeetingEntity
import java.time.LocalDateTime

/**
 * Репозиторий для работы с встречами через API
 */
class MeetingRepository(
    private val dataSource: MeetingDataSource
) {
    suspend fun getBookingToMe() : Result<List<MeetingEntity>> {
        return dataSource.getAllBookings().map { listDto ->
            listDto.you?.map { bookingDto ->
                MeetingEntity(
                    name = bookingDto.name ?: "",
                    employeeAdmin = bookingDto.employeeAdmin ?: "",
                    startBooking = bookingDto.startBooking?.toLocalDateTime() ?: LocalDateTime.now(),
                    endBooking = bookingDto.endBooking?.toLocalDateTime() ?: LocalDateTime.now()
                )
            } ?: emptyList()
        }
    }

    suspend fun getBookingOutMe() : Result<List<MeetingEntity>> {
        return dataSource.getAllBookings().map { listDto ->
            listDto.yours?.map { bookingDto ->
                MeetingEntity(
                    name = bookingDto.name ?: "",
                    employeeAdmin = bookingDto.employeeAdmin ?: "",
                    startBooking = bookingDto.startBooking?.toLocalDateTime() ?: LocalDateTime.now(),
                    endBooking = bookingDto.endBooking?.toLocalDateTime() ?: LocalDateTime.now()
                )
            } ?: emptyList()
        }
    }

    fun String.toLocalDateTime() : LocalDateTime {
        var date = this.split(" ")[0].split(":")
        var time = this.split(" ")[1].split(":")
        return LocalDateTime.of(date[0].toInt(), date[1].toInt(), date[2].toInt(),
            time[0].toInt(), time[1].toInt())
    }



}
