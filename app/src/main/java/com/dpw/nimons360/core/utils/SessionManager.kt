package com.dpw.nimons360.core.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
@Suppress("DEPRECATION")

class SessionManager(context: Context) {

    private val appContext = context.applicationContext

    private val preferences: SharedPreferences by lazy {
        val encryptedPrefs = createEncryptedPreferences(appContext)
        migrateLegacyIfNeeded(appContext, encryptedPrefs)
        encryptedPrefs
    }

    fun isLoggedIn(): Boolean = getToken().isNotBlank()

    fun saveSession(token: String, expiresAt: String?, fullName: String?, email: String?) {
        preferences.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_EXPIRES_AT, expiresAt)
            .putString(KEY_FULL_NAME, fullName)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun getToken(): String = preferences.getString(KEY_TOKEN, "").orEmpty()

    fun clearSession() {
        preferences.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_EXPIRES_AT)
            .remove(KEY_FULL_NAME)
            .remove(KEY_EMAIL)
            .apply()
    }

    private fun createEncryptedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            SECURE_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun migrateLegacyIfNeeded(context: Context, encryptedPrefs: SharedPreferences) {
        val legacyPrefs = context.getSharedPreferences(LEGACY_PREF_NAME, Context.MODE_PRIVATE)
        val encryptedHasToken = encryptedPrefs.getString(KEY_TOKEN, null) != null
        if (encryptedHasToken) return

        val legacyToken = legacyPrefs.getString(KEY_TOKEN, null) ?: return

        encryptedPrefs.edit()
            .putString(KEY_TOKEN, legacyToken)
            .putString(KEY_EXPIRES_AT, legacyPrefs.getString(KEY_EXPIRES_AT, null))
            .putString(KEY_FULL_NAME, legacyPrefs.getString(KEY_FULL_NAME, null))
            .putString(KEY_EMAIL, legacyPrefs.getString(KEY_EMAIL, null))
            .apply()

        legacyPrefs.edit().clear().apply()
    }

    companion object {
        private const val LEGACY_PREF_NAME = "nimons_session"
        private const val SECURE_PREF_NAME = "nimons_session_secure"
        private const val KEY_TOKEN = "token"
        private const val KEY_EXPIRES_AT = "expires_at"
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_EMAIL = "email"
    }
}

