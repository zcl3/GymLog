package com.personal.gymlog.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore("gymlog_settings")

class SettingsRepository(private val context: Context) {
    private object Keys {
        val unit = stringPreferencesKey("unit")
        val rest = intPreferencesKey("default_rest_seconds")
        val waterGoal = intPreferencesKey("daily_water_goal_ml")
        val reminderEnabled = booleanPreferencesKey("water_reminder_enabled")
        val reminderStart = intPreferencesKey("reminder_start_minutes")
        val reminderEnd = intPreferencesKey("reminder_end_minutes")
        val reminderInterval = intPreferencesKey("reminder_interval_minutes")
        val currentDate = stringPreferencesKey("current_date")
        val fontScale = floatPreferencesKey("font_scale")
    }

    val settings: Flow<AppSettings> = context.settingsDataStore.data.map { preferences ->
        AppSettings(
            unit = preferences[Keys.unit] ?: "kg",
            defaultRestSeconds = preferences[Keys.rest] ?: 90,
            dailyWaterGoalMl = preferences[Keys.waterGoal] ?: 2500,
            waterReminderEnabled = preferences[Keys.reminderEnabled] ?: false,
            reminderStartMinutes = preferences[Keys.reminderStart] ?: 540,
            reminderEndMinutes = preferences[Keys.reminderEnd] ?: 1320,
            reminderIntervalMinutes = preferences[Keys.reminderInterval] ?: 90,
            currentDate = preferences[Keys.currentDate],
            fontScale = preferences[Keys.fontScale] ?: 1f,
        )
    }

    suspend fun setUnit(unit: String) = context.settingsDataStore.edit { it[Keys.unit] = unit }
    suspend fun setWaterGoalMl(goalMl: Int) = context.settingsDataStore.edit { it[Keys.waterGoal] = goalMl.coerceAtLeast(0) }
    suspend fun setCurrentDate(date: String?) = context.settingsDataStore.edit { if (date.isNullOrBlank()) it.remove(Keys.currentDate) else it[Keys.currentDate] = date }
    suspend fun setFontScale(scale: Float) = context.settingsDataStore.edit { it[Keys.fontScale] = scale.coerceIn(0.85f, 1.3f) }
}
