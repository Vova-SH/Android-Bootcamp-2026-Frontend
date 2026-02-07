package com.teto.planner.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

data class AuthCredentials(
    val login: String,
    val pass: String
)

@Singleton
class CredentialsHolder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoManager: CryptoManager
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val LOGIN_KEY = stringPreferencesKey("encrypted_login")
    private val PASS_KEY = stringPreferencesKey("encrypted_pass")

    val credentialsFlow: StateFlow<AuthCredentials?> = context.dataStore.data
        .map { prefs ->
            val encLogin = prefs[LOGIN_KEY]
            val encPass = prefs[PASS_KEY]

            if (!encLogin.isNullOrBlank() && !encPass.isNullOrBlank()) {
                AuthCredentials(
                    login = cryptoManager.decryptString(encLogin),
                    pass = cryptoManager.decryptString(encPass)
                )
            } else {
                null
            }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun setCredentials(l: String, p: String) {
        scope.launch {
            val encLogin = cryptoManager.encryptString(l)
            val encPass = cryptoManager.encryptString(p)

            context.dataStore.edit { prefs ->
                prefs[LOGIN_KEY] = encLogin
                prefs[PASS_KEY] = encPass
            }
        }
    }

    fun clear() {
        scope.launch {
            context.dataStore.edit { it.clear() }
        }
    }

    fun hasCredentialsNow(): Boolean {
        return credentialsFlow.value != null
    }

    suspend fun getCredentials(): AuthCredentials? {
        val prefs = context.dataStore.data.first()
        val encLogin = prefs[LOGIN_KEY]
        val encPass = prefs[PASS_KEY]

        if (!encLogin.isNullOrBlank() && !encPass.isNullOrBlank()) {
            return AuthCredentials(
                login = cryptoManager.decryptString(encLogin),
                pass = cryptoManager.decryptString(encPass)
            )
        }
        return null
    }
}