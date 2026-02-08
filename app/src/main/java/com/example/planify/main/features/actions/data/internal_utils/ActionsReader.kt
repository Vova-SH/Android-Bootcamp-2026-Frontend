package com.example.planify.main.features.actions.data.internal_utils

import android.util.Log
import com.example.planify.core.exceptions.UnauthenticatedAppError
import com.example.planify.main.features.actions.domain.entities.Action
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive

typealias ActionsFetcher = suspend () -> Result<List<Action<*>>>
typealias ActionsInitializer = suspend () -> Result<List<Action<*>>>

class ActionsReader(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val fetcher: ActionsFetcher,
    private val initializer: ActionsInitializer
) {
    private val readerJob = SupervisorJob()
    private val readerCoroutineScope = CoroutineScope(dispatcher + readerJob)

    val actionsFlow: SharedFlow<Action<*>> =
        combinedFlow()
            .shareIn(
                scope = readerCoroutineScope,
                started = SharingStarted.WhileSubscribed(
                    stopTimeoutMillis = 5_000
                ),
                replay = 0
            )

    private fun combinedFlow(): Flow<Action<*>> = flow {
        val local = initializer().getOrThrow()
        local.forEach { emit(it) }
        emitAll(pollingFlow())
    }

    private fun pollingFlow(): Flow<Action<*>> = callbackFlow {
        while (currentCoroutineContext().isActive) {
            try {
                val actions = fetcher().getOrThrow()
                actions.forEach { trySend(it) }
            } catch (error: CancellationException) {
                throw error
            } catch (error: UnauthenticatedAppError) {
                Log.w(this::class.simpleName, "Failed to fetch actions (Unauthenticated): ${error::class.simpleName}: ${error.message}, stopping reader")
                break
            } catch (error: Exception) {
                Log.w(this::class.simpleName, "Failed to fetch actions: ${error::class.simpleName}: ${error.message}, stopping reader")
                break
            }
        }

        awaitClose { }
    }
}
