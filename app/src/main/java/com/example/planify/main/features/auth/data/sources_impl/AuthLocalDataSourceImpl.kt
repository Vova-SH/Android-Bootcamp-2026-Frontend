package com.example.planify.main.features.auth.data.sources_impl

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.planify.core.data.serializers.jsonCore
import com.example.planify.main.features.auth.data.preferences.AuthDataStoreInfo
import com.example.planify.main.features.auth.data.preferences.authSecuredDatastore
import com.example.planify.main.features.auth.data.sources.AuthLocalDataSource
import com.example.planify.main.features.auth.domain.schemas.AuthLocalInfoSchema
import com.google.crypto.tink.Aead
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthLocalDataSourceImpl @Inject constructor(
    private val context: Context,
    private val aead: Aead
) : AuthLocalDataSource {
    override val authInfoFlow: Flow<AuthLocalInfoSchema?> = context.authSecuredDatastore.data.map { preferences ->
        val b64 = preferences[AuthDataStoreInfo.INFO_KEY] ?: return@map null

        try {
            val encrypted = Base64.decode(b64, Base64.NO_WRAP)
            val decrypted = aead.decrypt(encrypted, null)
            jsonCore.decodeFromString<AuthLocalInfoSchema>(decrypted.decodeToString())
        } catch (e: Exception) {
            Log.e("SecureAuthInfoStorage", "Failed to load auth info: ${e.message}")
            return@map null
        }
    }

    override suspend fun saveAuthInfo(info: AuthLocalInfoSchema) {
        val json = jsonCore.encodeToString(info)
        val encrypted = aead.encrypt(json.toByteArray(), null)
        val b64 = Base64.encodeToString(encrypted, Base64.NO_WRAP)

        context.authSecuredDatastore.edit {
            it[AuthDataStoreInfo.INFO_KEY] = b64
        }
    }

    override suspend fun clearAuthInfo() {
        context.authSecuredDatastore.edit {
            it.remove(AuthDataStoreInfo.INFO_KEY)
        }
    }
}