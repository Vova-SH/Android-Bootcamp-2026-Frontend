package com.example.planify.main.features.actions.data.sources

import com.example.planify.main.features.actions.data.models.ActionModel
import com.example.planify.main.features.actions.domain.entities.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer

interface ActionsLocalDataSource {
    suspend fun saveAction(id: String, type: String, data: String?): Result<ActionModel>
    suspend fun <T : Any> saveAction(action: Action<T>, serializer: KSerializer<T>): Result<ActionModel>
    suspend fun getAllActions(): Result<List<ActionModel>>
    suspend fun deleteAction(actionId: String): Result<Unit>

    suspend fun getLastSeenActionId(): String
    suspend fun setLastSeenActionId(actionId: String)
}
