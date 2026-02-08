package com.example.planify.main.features.actions.data.repositories_impl

import com.example.planify.main.features.actions.data.internal_utils.ActionsReader
import com.example.planify.main.features.actions.data.sources.ActionsLocalDataSource
import com.example.planify.main.features.actions.data.sources.ActionsRemoteDataSource
import com.example.planify.main.features.actions.domain.entities.Action
import com.example.planify.main.features.actions.domain.exceptions.BadActionIdHttpException
import com.example.planify.main.features.actions.domain.repositories.ActionsRepository
import com.example.planify.main.features.actions.domain.utils.ActionDataParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import javax.inject.Inject

class ActionsRepositoryImpl @Inject constructor(
    private val remoteDataSource: ActionsRemoteDataSource,
    private val localDataSource: ActionsLocalDataSource,
    private val actionDataParser: ActionDataParser
) : ActionsRepository {
    private val actionsReader: ActionsReader = ActionsReader(dispatcher = Dispatchers.IO, fetcher = this::fetchActions, initializer = this::getAllActionLocal)

    override val actionsFlow: SharedFlow<Action<*>> = actionsReader.actionsFlow

    private suspend fun fetchActionsInternal(attempt: Int): Result<List<Action<*>>> {
        val lastSeen = localDataSource.getLastSeenActionId()

        return remoteDataSource.fetchActions(lastSeen)
            .recover { error ->
                if (attempt == 0 && error is BadActionIdHttpException) {
                    localDataSource.setLastSeenActionId("0-0")  // Reset, read from start
                    fetchActionsInternal(1).getOrThrow()  // To load history to local db
                    return Result.success(emptyList())
                }

                return Result.failure(error)
            }
            .onSuccess { actionDTOs ->
                actionDTOs.forEach { action ->
                    saveActionToLocalDB(
                        id = action.id,
                        type = action.type,
                        data = action.data?.let { actionDataParser.serializeJsonElement(it) }
                    ).getOrThrow()
                }
            }
            .map { actions -> actions.map { it.toEntity(actionDataParser) } }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun fetchActions(): Result<List<Action<*>>> {  // TODO: Check if i lastSeenActionId is valid
        return fetchActionsInternal(0)
    }

    override suspend fun deleteAction(actionId: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            remoteDataSource.deleteAction(actionId).getOrThrow()
            localDataSource.deleteAction(actionId).getOrThrow()
        }
    }

    override suspend fun getAllActionLocal(): Result<List<Action<*>>> {
        return localDataSource.getAllActions().map { actions -> actions.map { it.toEntity(actionDataParser) } }
    }

    override suspend fun <T : Any> saveActionToLocalDB(action: Action<T>, serializer: KSerializer<T>): Result<Unit> {
        return localDataSource.saveAction(action, serializer)
            .onSuccess { localDataSource.setLastSeenActionId(action.id) }
            .map { }
    }

    override suspend fun saveActionToLocalDB(id: String, type: String, data: String?): Result<Unit> {
        return localDataSource.saveAction(id, type, data)
            .onSuccess { localDataSource.setLastSeenActionId(id) }
            .map { }
    }
}
