package com.example.planify.main.features.actions.domain.services_impl

import com.example.planify.main.features.actions.domain.entities.Action
import com.example.planify.main.features.actions.domain.repositories.ActionsRepository
import com.example.planify.main.features.actions.domain.services.ActionsService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.KSerializer

class ActionsServiceImpl @Inject constructor(
    private val actionsRepository: ActionsRepository
) : ActionsService {
    override val actionsFlow: SharedFlow<Action<*>> = actionsRepository.actionsFlow

    override suspend fun fetchActions(): Result<List<Action<*>>> {
        return actionsRepository.fetchActions()
    }

    override suspend fun deleteAction(actionId: String): Result<Unit> {
        return actionsRepository.deleteAction(actionId)
    }

    override suspend fun getAllActionLocal(): Result<List<Action<*>>> {
        return actionsRepository.getAllActionLocal()
    }

    override suspend fun <T : Any> saveActionToLocalDB(action: Action<T>, serializer: KSerializer<T>): Result<Unit> {
        return actionsRepository.saveActionToLocalDB(action, serializer)
    }
}
