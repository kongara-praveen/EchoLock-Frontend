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
    private val ENCRYPTION_NOTIF_KEY = booleanPreferencesKey("encryption_notifications")
    private val DECRYPTION_NOTIF_KEY = booleanPreferencesKey("decryption_notifications")
    private val TAMPER_NOTIF_KEY = booleanPreferencesKey("tamper_notifications")
    private val SOUND_ENABLED_KEY = booleanPreferencesKey("notification_sound")
    private val VIBRATION_ENABLED_KEY = booleanPreferencesKey("notification_vibration")

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

    fun isEncryptionEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[ENCRYPTION_NOTIF_KEY] ?: true
        }
    }

    suspend fun setEncryptionEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ENCRYPTION_NOTIF_KEY] = enabled
        }
    }

    fun isDecryptionEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[DECRYPTION_NOTIF_KEY] ?: true
        }
    }

    suspend fun setDecryptionEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DECRYPTION_NOTIF_KEY] = enabled
        }
    }

    fun isTamperEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[TAMPER_NOTIF_KEY] ?: true
        }
    }

    suspend fun setTamperEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TAMPER_NOTIF_KEY] = enabled
        }
    }

    fun isSoundEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[SOUND_ENABLED_KEY] ?: true
        }
    }

    suspend fun setSoundEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SOUND_ENABLED_KEY] = enabled
        }
    }

    fun isVibrationEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[VIBRATION_ENABLED_KEY] ?: true
        }
    }

    suspend fun setVibrationEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[VIBRATION_ENABLED_KEY] = enabled
        }
    }
}
