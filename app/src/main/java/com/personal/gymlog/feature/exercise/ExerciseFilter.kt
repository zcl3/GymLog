package com.personal.gymlog.feature.exercise

import com.personal.gymlog.data.local.entity.Exercise

fun filterExercises(exercises: List<Exercise>, query: String, bodyPart: String?): List<Exercise> {
    val normalized = query.trim()
    return exercises.filter { exercise ->
        (bodyPart == null || exercise.bodyPart == bodyPart) &&
            (normalized.isEmpty() || exercise.name.contains(normalized, ignoreCase = true))
    }
}
