package com.example.planify.main.features.actions.domain.entities

data class ActionsLocalInfo(
    val lastSeenActionId: String,
    val a: Int = 0
) {
    constructor(
        lastSeenActionId: String? = null
    ) : this(
        lastSeenActionId = lastSeenActionId ?: ActionsLocalInfoDefaults.LAST_SEEN_ACTION_ID
    )
}
