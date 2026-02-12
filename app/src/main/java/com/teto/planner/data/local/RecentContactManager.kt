package com.teto.planner.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.teto.planner.domain.model.user.LoadStatus
import com.teto.planner.domain.model.user.UserSummary
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "recent_contacts_store")

@Singleton
class RecentContactManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val KEY_RECENT_USERS = stringPreferencesKey("recent_users_json")
    private val MAX_RECENT_COUNT = 10

    val recentUsers: StateFlow<List<UserSummary>> = context.dataStore.data
        .map { prefs ->
            val jsonString = prefs[KEY_RECENT_USERS]
            parseUsers(jsonString)
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    suspend fun addRecentUsers(newUsers: List<UserSummary>) {
        if (newUsers.isEmpty()) return

        context.dataStore.edit { prefs ->
            val currentJson = prefs[KEY_RECENT_USERS]
            val currentList = parseUsers(currentJson).toMutableList()

            newUsers.forEach { newUser ->
                currentList.removeAll { it.id == newUser.id }
                currentList.add(0, newUser)
            }

            val trimmedList = currentList.take(MAX_RECENT_COUNT)

            prefs[KEY_RECENT_USERS] = serializeUsers(trimmedList)
        }
    }

    private fun parseUsers(jsonString: String?): List<UserSummary> {
        if (jsonString.isNullOrBlank()) return emptyList()
        return try {
            val jsonArray = JSONArray(jsonString)
            val users = mutableListOf<UserSummary>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val statusStr = obj.optString("loadStatus", "")
                val loadStatus = if (statusStr.isNotEmpty()) {
                    try { LoadStatus.valueOf(statusStr) } catch (e: Exception) { LoadStatus.LOW }
                } else LoadStatus.LOW

                users.add(
                    UserSummary(
                        id = obj.getString("id"),
                        name = obj.getString("name"),
                        avatarUrl = if (obj.has("avatarUrl") && !obj.isNull("avatarUrl")) obj.getString("avatarUrl") else null,
                        telegram = if (obj.has("telegram") && !obj.isNull("telegram")) obj.getString("telegram") else null,
                        bio = if (obj.has("bio") && !obj.isNull("bio")) obj.getString("bio") else null,
                        busyHours = obj.optInt("busyHours", 0),
                        loadStatus = loadStatus,
                        updatedAt = if (obj.has("updatedAt") && !obj.isNull("updatedAt")) obj.getString("updatedAt") else null
                    )
                )
            }
            users
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun serializeUsers(users: List<UserSummary>): String {
        return try {
            val jsonArray = JSONArray()
            users.forEach { user ->
                val obj = JSONObject()
                obj.put("id", user.id)
                obj.put("name", user.name)

                if (user.avatarUrl != null) obj.put("avatarUrl", user.avatarUrl)
                if (user.telegram != null) obj.put("telegram", user.telegram)
                if (user.bio != null) obj.put("bio", user.bio)
                if (user.updatedAt != null) obj.put("updatedAt", user.updatedAt)

                obj.put("busyHours", user.busyHours)
                obj.put("loadStatus", user.loadStatus.name)

                jsonArray.put(obj)
            }
            jsonArray.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}