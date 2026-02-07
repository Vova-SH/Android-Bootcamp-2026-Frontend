package ru.sicampus.bootcamp2026.android.data

import ru.sicampus.bootcamp2026.android.data.dto.PersonDto
import ru.sicampus.bootcamp2026.android.data.source.PersonsNetworkDataSource

class PersonsRepository(
    private val network: PersonsNetworkDataSource
) {
    suspend fun getPerson(id: Long): Result<PersonDto> = network.getPerson(id)
}