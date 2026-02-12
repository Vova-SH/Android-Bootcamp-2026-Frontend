package ru.sicampus.bootcamp2026.ui.screen.meetings

import kotlinx.collections.immutable.PersistentList
import ru.sicampus.bootcamp2026.domain.entities.EmployeeListEntity

sealed interface EmployeeSearchState {
    data class Error(
        val reason: String
    ): EmployeeSearchState
    data object Loading: EmployeeSearchState
    data class Content(
        val users: PersistentList<Item>,
        val inputError: String? = null,
        val isLastPage: Boolean
    ) : EmployeeSearchState
    sealed interface Item{
        data object Loading : Item
        data object Error: Item
        data class Employee(val entity: EmployeeListEntity) : Item
    }
}