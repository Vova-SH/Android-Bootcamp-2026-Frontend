package com.example.planify.main.features.auth.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.authSecuredDatastore by preferencesDataStore(name = "auth_secured_store")

object AuthDataStoreInfo {
    val INFO_KEY = stringPreferencesKey("tokens")
}
