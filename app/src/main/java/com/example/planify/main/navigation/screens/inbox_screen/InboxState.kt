package com.example.planify.main.navigation.screens.inbox_screen

import com.example.planify.core.ui.state.ResourceState

data class InboxState (
    val actions: Map<String, ResourceState<InboxAction>>
) {
    companion object {
        fun empty() = InboxState(actions = emptyMap())
    }
}