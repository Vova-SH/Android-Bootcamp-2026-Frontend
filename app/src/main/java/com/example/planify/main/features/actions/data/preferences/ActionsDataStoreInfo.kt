package com.example.planify.main.features.actions.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.actionsDataStore by preferencesDataStore("actions_store")

object ActionsDataStoreInfo {
    val LAST_SEEN_ACTION_ID_KEY = stringPreferencesKey("last_seen_action_id_key")
}
