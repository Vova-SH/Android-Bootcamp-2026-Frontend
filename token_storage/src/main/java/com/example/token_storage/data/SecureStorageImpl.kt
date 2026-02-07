package com.example.token_storage.data

import androidx.datastore.preferences.core.stringPreferencesKey
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.token_storage.domain.SecureStorage
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dagger.hilt.android.qualifiers.ApplicationContext
import com.google.crypto.tink.aead.AesGcmKeyManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import java.util.Base64

private val Context.dataStore by preferencesDataStore(name = "secure_datastore")


@Singleton
class SecureStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SecureStorage {

    private val aead: Aead = run {

        AeadConfig.register()

        val keysetHandle: KeysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "tink_keyset", "tink_prefs")
            .withMasterKeyUri("android-keystore://jwt_master_key")
            .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
            .build()
            .keysetHandle

        keysetHandle.getPrimitive(Aead::class.java)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getString(key: String): String? {

        val keyPrefs = stringPreferencesKey(key)

        val base64Encrypted = context.dataStore.data.map { it[keyPrefs] }.firstOrNull()

        return base64Encrypted?.let { encrypted ->
            val encryptedBytes = Base64.getDecoder().decode(encrypted)
            val decryptedBytes = aead.decrypt(encryptedBytes, null)
            String(decryptedBytes, Charsets.UTF_8)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun putString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        val encryptedBytes = aead.encrypt(value.toByteArray(Charsets.UTF_8), null)
        val base64Encrypted = Base64.getEncoder().encodeToString(encryptedBytes)

        context.dataStore.edit { prefs ->
            prefs[prefKey] = base64Encrypted
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}