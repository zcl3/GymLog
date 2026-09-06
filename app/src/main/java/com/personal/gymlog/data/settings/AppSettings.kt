package com.personal.gymlog.data.settings

import java.time.LocalDate

data class AppSettings(
    val unit: String = "kg",
    val defaultRestSeconds: Int = 90,
    val dailyWaterGoalMl: Int = 2500,
    val waterReminderEnabled: Boolean = false,
    val reminderStartMinutes: Int = 9 * 60,
    val reminderEndMinutes: Int = 22 * 60,
    val reminderIntervalMinutes: Int = 90,
    val currentDate: String? = null,
    val fontScale: Float = 1f,
)

/** The day whose records are being viewed or added.  A malformed saved value
 * falls back to today instead of making the whole app unusable. */
fun AppSettings.recordDate(): String = currentDate
    ?.let { runCatching { LocalDate.parse(it).toString() }.getOrNull() }
    ?: LocalDate.now().toString()
