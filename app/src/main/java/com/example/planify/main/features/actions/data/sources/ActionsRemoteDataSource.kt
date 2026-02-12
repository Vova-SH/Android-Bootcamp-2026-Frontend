package com.example.planify.main.features.actions.data.sources

import com.example.planify.main.features.actions.data.dto.ActionDTO

interface ActionsRemoteDataSource {
    suspend fun fetchActions(lastSeen: String): Result<List<ActionDTO>>
    suspend fun deleteAction(actionId: String): Result<Unit>
}
