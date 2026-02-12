package ru.sicampus.bootcamp2026.data.repository

import ru.sicampus.bootcamp2026.data.dto.toEntity
import ru.sicampus.bootcamp2026.data.source.EmployeeSearchDataSource
import ru.sicampus.bootcamp2026.domain.entities.PagingEmployeeListEntity

class EmployeeRepository(
    private val employeeListDataSource: EmployeeSearchDataSource
) {
    suspend fun searchEmployees(query: String?, page: Int, size: Int): Result<PagingEmployeeListEntity>{

        return employeeListDataSource.searchEmployees(query, page = page, size = size).mapCatching{ dto ->
            PagingEmployeeListEntity(
                isLast = dto.last ?: true,
                users = dto.content?.map { it.toEntity() } ?: error("List is null")
            )
        }
    }
}