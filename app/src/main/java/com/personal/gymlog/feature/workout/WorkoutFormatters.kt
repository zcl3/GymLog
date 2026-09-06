package com.personal.gymlog.feature.workout

import com.personal.gymlog.data.local.entity.WorkoutSession
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val clockFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun workoutStartTime(session: WorkoutSession): String = Instant.ofEpochMilli(session.startedAt)
    .atZone(ZoneId.of(session.zoneId))
    .format(clockFormatter)

fun workoutVolumeKg(weightGrams: Int, reps: Int): Double = weightGrams / 1000.0 * reps
