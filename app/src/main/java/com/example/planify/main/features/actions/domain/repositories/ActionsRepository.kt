package com.example.planify.main.features.actions.domain.repositories

import com.example.planify.main.features.actions.domain.entities.Action
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.KSerializer

interface ActionsRepository {
    val actionsFlow: SharedFlow<Action<*>>

    suspend fun fetchActions(): Result<List<Action<*>>>

    suspend fun deleteAction(actionId: String): Result<Unit>

    suspend fun getAllActionLocal(): Result<List<Action<*>>>

    suspend fun <T : Any> saveActionToLocalDB(action: Action<T>, serializer: KSerializer<T>): Result<Unit>

    suspend fun saveActionToLocalDB(id: String, type: String, data: String?): Result<Unit>
}
