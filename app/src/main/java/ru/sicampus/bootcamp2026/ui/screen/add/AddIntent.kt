package ru.sicampus.bootcamp2026.ui.screen.add

import ru.sicampus.bootcamp2026.ui.screen.list.ListIntent

sealed interface AddIntent {
    data object LoadMore: AddIntent
    data object Refresh: AddIntent
}