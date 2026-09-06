package com.personal.gymlog.feature.workout

import com.personal.gymlog.data.local.entity.WorkoutSession
import com.personal.gymlog.data.local.relation.WorkoutDetails
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val clockFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun workoutStartTime(session: WorkoutSession): String = Instant.ofEpochMilli(session.startedAt)
    .atZone(ZoneId.of(session.zoneId))
    .format(clockFormatter)

fun workoutVolumeKg(weightGrams: Int, reps: Int): Double = weightGrams / 1000.0 * reps

fun workoutSummary(detail: WorkoutDetails): String = buildList {
    add("开始于 ${workoutStartTime(detail.session)}")
    detail.exercises.forEach { exercise ->
        add(exercise.exercise.nameSnapshot)
        exercise.sets.forEach { set ->
            val weight = if (set.weightGrams > 0) "${formatWeightKg(set.weightGrams)} kg" else "未填写重量"
            val reps = if (set.reps > 0) set.reps.toString() else "未填写次数"
            val status = if (set.isCompleted) "" else "（未完成）"
            add("$weight × $reps$status")
        }
    }
}.joinToString("\n")

fun formatWeightKg(weightGrams: Int): String {
    val kg = weightGrams / 1000.0
    return if (kg % 1.0 == 0.0) kg.toInt().toString() else "%.1f".format(kg)
}
