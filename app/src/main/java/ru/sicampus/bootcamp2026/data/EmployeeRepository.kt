package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.EmployeeSearchDataSource
import ru.sicampus.bootcamp2026.domain.entities.EmployeeEntity
import ru.sicampus.bootcamp2026.domain.entities.PagingEmployeeListEntity

class EmployeeRepository(
    private val employeeListDataSource: EmployeeSearchDataSource
) {
    suspend fun searchEmployees(query: String?, page: Int, size: Int): Result<PagingEmployeeListEntity>{

        return employeeListDataSource.searchEmployees(query, page = page, size = size).mapCatching{ dto ->
            PagingEmployeeListEntity(
                isLast = dto.last ?: true,
                users = dto.content?.mapNotNull { employeeDTO ->
                    EmployeeEntity(
                        name = employeeDTO.name ?: return@mapNotNull null,
                        position = employeeDTO.position ?: return@mapNotNull null,
                        username = employeeDTO.username ?: return@mapNotNull null,
                        email = employeeDTO.email ?: return@mapNotNull null,
                        phoneNumber = employeeDTO.phoneNumber ?: return@mapNotNull null,
                        photoUrl = employeeDTO.photoUrl ?: ""
                    )
                } ?: error("List is null")
            )


        }
    }
}