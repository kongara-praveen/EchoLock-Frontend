package com.example.echolock.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* ---------- DATASTORE EXTENSION ---------- */
private val Context.dataStore by preferencesDataStore(
    name = "echolock_settings"
)

/* ---------- NOTIFICATION PREFS ---------- */
object NotificationPrefs {

    private val NOTIFICATION_KEY = booleanPreferencesKey("notifications_enabled")

    fun isEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[NOTIFICATION_KEY] ?: true
        }
    }

    suspend fun setEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = enabled
        }
    }
}
