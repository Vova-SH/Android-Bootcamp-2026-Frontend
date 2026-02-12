package ru.sicampus.bootcamp2026.ui.screens.profile

import ru.sicampus.bootcamp2026.domain.entities.UserMini

data class UserProfileUiData(
    val fullName: String = "",
    val email: String = "",
    val position: String? = null,
    val department: String? = null,
    val description: String? = null,
    val photoUrl: String = "",

    val updateFirstName: String = "",
    val updateSecondName: String = "",
    val updatePosition:  String? = null,
    val updateDepartment:  String? = null,
    val updateDescription:  String? = null,

    val search: String = "",
    val searchResults: List<UserMini> = emptyList(),
    val currentSearchPage: Long = 0,
    val isLoading: Boolean = false,
    val isLastPage: Boolean = false,

    val errorMessage: String? = null
)