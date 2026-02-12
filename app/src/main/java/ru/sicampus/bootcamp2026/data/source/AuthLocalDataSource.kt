package ru.sicampus.bootcamp2026.data.source
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlin.io.encoding.ExperimentalEncodingApi

object AuthLocalDataSource {
    private var isInit = false
    private var _cashToken: String? = null

    suspend fun getToken(): String? {

//        if (!isInit) {
//            _cashToken  = App.context.dataStore.data.map { preferences ->
//                preferences[TOKEN]
//            }.firstOrNull()
//            isInit = true
//        }
        return "_cashToken"
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun setToken(login: String, password: String) {
//        val decodePhrase = "$login:$password"
//        val token = "Basic ${Base64.encode(decodePhrase.toByteArray())}"
//        _cashToken = token
//        App.context.dataStore.updateData { pref ->
//            pref.toMutablePreferences().also { preferences ->
//                preferences[TOKEN] = token
//            }
//        }
    }

    fun clearToken() {

    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val TOKEN = stringPreferencesKey("token")
}