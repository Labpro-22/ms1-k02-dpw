package com.dpw.nimons360.core.utils

import android.content.Context

class SessionManager(context: Context) {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean = preferences.getBoolean(KEY_IS_LOGGED_IN, false)

    fun setLoggedIn(loggedIn: Boolean) {
        preferences.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }

    companion object {
        private const val PREF_NAME = "nimons_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
}

