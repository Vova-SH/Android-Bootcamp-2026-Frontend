package com.example.planify.main.features.actions.data.sources_impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.planify.core.data.serializers.jsonCore
import com.example.planify.main.features.actions.data.dao.ActionModelDAO
import com.example.planify.main.features.actions.data.models.ActionModel
import com.example.planify.main.features.actions.data.preferences.ActionsDataStoreInfo
import com.example.planify.main.features.actions.data.preferences.actionsDataStore
import com.example.planify.main.features.actions.data.sources.ActionsLocalDataSource
import com.example.planify.main.features.actions.domain.entities.Action
import com.example.planify.main.features.actions.domain.entities.ActionsLocalInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionsLocalDataSourceImpl @Inject constructor(
    context: Context,
    private val actionModelDAO: ActionModelDAO
) : ActionsLocalDataSource {
    private val dataStore = context.actionsDataStore

    private fun fromPrimitives(
        lastSeenActionId: String?
    ): ActionsLocalInfo {
        return ActionsLocalInfo(
            lastSeenActionId = lastSeenActionId
        )
    }

    val actionsLocalFlow: Flow<ActionsLocalInfo> = dataStore.data.map { preferences ->
        fromPrimitives(
            lastSeenActionId = preferences[ActionsDataStoreInfo.LAST_SEEN_ACTION_ID_KEY]
        )
    }

    override suspend fun <T : Any> saveAction(action: Action<T>, serializer: KSerializer<T>): Result<ActionModel> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val model = ActionModel(
                id = action.id,
                type = action.type,
                data = action.data?.let { jsonCore.encodeToString(serializer, action.data) }
            )

            actionModelDAO.upsert(model)

            model
        }
    }

    override suspend fun saveAction(id: String, type: String, data: String?): Result<ActionModel> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val model = ActionModel(id, type, data)
            actionModelDAO.upsert(model)
            model
        }
    }

    override suspend fun getAllActions(): Result<List<ActionModel>> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            actionModelDAO.getAll()
        }
    }

    override suspend fun deleteAction(actionId: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            actionModelDAO.deleteActionById(actionId)
        }
    }

    override suspend fun getLastSeenActionId(): String {
        return actionsLocalFlow.first().lastSeenActionId
    }

    override suspend fun setLastSeenActionId(actionId: String) {
        dataStore.edit { preferences ->
            preferences[ActionsDataStoreInfo.LAST_SEEN_ACTION_ID_KEY] = actionId
        }
    }
}
