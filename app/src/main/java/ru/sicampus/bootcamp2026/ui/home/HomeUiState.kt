package ru.sicampus.bootcamp2026.ui.home

import java.time.LocalTime
import java.util.Calendar
import java.util.SortedMap

data class HomeUiState(
    val username: String = "",
    val userAvatarUrl: String? = null,

    val greeting: String = "",

//    val createdMeetings: List<Meeting> = emptyList(),//все встречи созданные пользователем
//    val plannedMeetings: List<Meeting> = emptyList(),//все подтвержденные встречи

    val isLoading: Boolean,
    val errorMessagre: String? = null,

    val sortOrder: SortOrder = SortOrder.DESCENDING,
    val isSortDropdownExpanded: Boolean = false
)
enum class SortOrder(val label: String) {// для фильтрации по убыванию и возрастанию
    DESCENDING("descending"),
    ASCENDING("ascending")
}
