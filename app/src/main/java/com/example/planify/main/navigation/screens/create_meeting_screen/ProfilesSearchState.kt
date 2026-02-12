package com.example.planify.main.navigation.screens.create_meeting_screen

data class ProfilesSearchState(
    val query: String,
    val page: Int
) {
    companion object {
        fun empty(): ProfilesSearchState = ProfilesSearchState(
            query = "",
            page = 0
        )
    }
}
