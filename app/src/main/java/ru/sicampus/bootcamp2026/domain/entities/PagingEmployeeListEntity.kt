package ru.sicampus.bootcamp2026.domain.entities

data class PagingEmployeeListEntity (
    val isLast: Boolean,
    val users: List<EmployeeListEntity>
)