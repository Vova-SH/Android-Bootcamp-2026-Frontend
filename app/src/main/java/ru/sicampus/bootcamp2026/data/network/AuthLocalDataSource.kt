package ru.sicampus.bootcamp2026.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object AuthLocalDataSource {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private var appContext: Context? = null
    @Volatile
    private var _token: String? = null

    fun init(context: Context) {
        appContext = context.applicationContext
        _token = runBlocking {
            appContext?.dataStore?.data?.map { it[TOKEN_KEY] }?.firstOrNull()
        }.let { if (it.isNullOrBlank()) null else it }
        Log.d("AuthLocalDataSource", "Initialized with token: $_token")
    }

    var token: String?
        get() = _token
        private set(value) {
            Log.d("AuthLocalDataSource", "Setting token: $value")
            _token = value
            Network.resetClient()
            GlobalScope.launch(Dispatchers.IO) {
                appContext?.dataStore?.edit { it[TOKEN_KEY] = value ?: "" }
            }
        }

    fun setToken(login: String, password: String) {
        val credentials = "${login.trim()}:${password.trim()}"
        val encoded = android.util.Base64.encodeToString(
            credentials.toByteArray(Charsets.UTF_8),
            android.util.Base64.NO_WRAP
        )
        token = "Basic $encoded"
        Log.d("AuthLocalDataSource", "New token generated for $login")
    }

    fun clearToken() {
        token = null
    }
}
