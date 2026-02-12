package com.example.planify.main.features.actions.domain.services

import com.example.planify.main.features.actions.domain.entities.Action
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

interface ActionsService {
    val actionsFlow: SharedFlow<Action<*>>

    suspend fun fetchActions(): Result<List<Action<*>>>

    suspend fun deleteAction(actionId: String): Result<Unit>

    suspend fun getAllActionLocal(): Result<List<Action<*>>>

    suspend fun <T : Any> saveActionToLocalDB(action: Action<T>, serializer: KSerializer<T>): Result<Unit>
}

suspend inline fun <reified T : Any> ActionsService.saveActionToLocalDB(action: Action<T>): Result<Unit> {
    return this.saveActionToLocalDB(action, serializer())
}
