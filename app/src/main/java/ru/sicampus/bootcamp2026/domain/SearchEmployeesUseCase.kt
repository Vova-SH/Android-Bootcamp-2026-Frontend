package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.repository.EmployeeRepository
import ru.sicampus.bootcamp2026.domain.entities.PagingEmployeeListEntity

class SearchEmployeesUseCase(
    private val employeeRepository: EmployeeRepository
){
    private val nameRegex = Regex("^[a-zA-Zа-яА-ЯёЁ\\s]*$")
    suspend operator fun invoke(
        offset: Int,
        query: String?
    ): Result<PagingEmployeeListEntity>{
        if (!query.isNullOrBlank() && !nameRegex.matches(query)) {
            return Result.failure(IllegalArgumentException("Строка содержит недопустимые символы"))
        }
        return employeeRepository.searchEmployees(
            query,
            page = offset / COUNT,
            size = COUNT
        )
    }
    private companion object{
        const val COUNT = 20
    }
}