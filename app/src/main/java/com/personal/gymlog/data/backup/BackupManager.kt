package com.personal.gymlog.data.backup

import com.personal.gymlog.data.local.entity.Exercise
import com.personal.gymlog.data.local.entity.WaterEntry

object BackupManager {
    fun export(exercises: List<Exercise>, water: List<WaterEntry>): String {
        val exerciseJson = exercises.joinToString(",") { "{\"id\":${it.id},\"name\":\"${escape(it.name)}\",\"bodyPart\":\"${escape(it.bodyPart)}\",\"isBuiltIn\":${it.isBuiltIn}}" }
        val waterJson = water.joinToString(",") { "{\"amountMl\":${it.amountMl},\"recordedAt\":${it.recordedAt},\"date\":\"${escape(it.date)}\"}" }
        return "{\"formatVersion\":1,\"exportedAt\":${System.currentTimeMillis()},\"exercises\":[$exerciseJson],\"water\":[$waterJson]}"
    }
    fun isSupported(json: String): Boolean = Regex("\\\"formatVersion\\\"\\s*:\\s*1").containsMatchIn(json)
    private fun escape(value: String) = value.replace("\\", "\\\\").replace("\"", "\\\"")
}
