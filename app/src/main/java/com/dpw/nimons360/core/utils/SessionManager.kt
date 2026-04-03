package com.dpw.nimons360.core.utils

import android.content.Context

class SessionManager(context: Context) {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

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

    fun getFullName(): String = preferences.getString(KEY_FULL_NAME, "").orEmpty()

    fun getEmail(): String = preferences.getString(KEY_EMAIL, "").orEmpty()

    fun clearSession() {
        preferences.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_EXPIRES_AT)
            .remove(KEY_FULL_NAME)
            .remove(KEY_EMAIL)
            .apply()
    }

    companion object {
        private const val PREF_NAME = "nimons_session"
        private const val KEY_TOKEN = "token"
        private const val KEY_EXPIRES_AT = "expires_at"
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_EMAIL = "email"
    }
}
