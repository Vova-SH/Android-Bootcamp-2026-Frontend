package com.example.meet.data.source

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.example.meet.data.dto.UserDto

object AuthPrefs {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_EMAIL = "email"
    private const val KEY_FULL_NAME = "full_name"

    private lateinit var encryptedPrefs: EncryptedSharedPreferences

    fun init(context: Context) {
        if (!::encryptedPrefs.isInitialized) {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            encryptedPrefs = EncryptedSharedPreferences.create(
                PREFS_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ) as EncryptedSharedPreferences
        }
    }


    fun saveLoginData(token: String, user: UserDto) {
        with(encryptedPrefs.edit()) {
            putString(KEY_TOKEN, token)
            putLong(KEY_USER_ID, user.id)
            putString(KEY_EMAIL, user.email)
            putString(KEY_FULL_NAME, user.fullName)
            apply()
        }
    }

    fun getToken(): String? = encryptedPrefs.getString(KEY_TOKEN, null)

    fun getUserData(): UserDto? {
        val userId = encryptedPrefs.getLong(KEY_USER_ID, -1)
        val email = encryptedPrefs.getString(KEY_EMAIL, null)
        val fullName = encryptedPrefs.getString(KEY_FULL_NAME, null)

        return if (userId != -1L && email != null && fullName != null) {
            UserDto(
                id = userId,
                email = email,
                fullName = fullName,
                passwordHash = null
            )
        } else {
            null
        }
    }

    fun clear() {
        with(encryptedPrefs.edit()) {
            clear()
            apply()
        }
    }
}
