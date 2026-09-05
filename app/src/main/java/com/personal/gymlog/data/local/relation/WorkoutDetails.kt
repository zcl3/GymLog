package com.personal.gymlog.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.personal.gymlog.data.local.entity.SetRecord
import com.personal.gymlog.data.local.entity.WorkoutExercise
import com.personal.gymlog.data.local.entity.WorkoutSession

data class WorkoutExerciseDetails(
    @Embedded val exercise: WorkoutExercise,
    @Relation(entity = SetRecord::class, parentColumn = "id", entityColumn = "workoutExerciseId") val sets: List<SetRecord>,
)

data class WorkoutDetails(
    @Embedded val session: WorkoutSession,
    @Relation(entity = WorkoutExercise::class, parentColumn = "id", entityColumn = "sessionId") val exercises: List<WorkoutExerciseDetails>,
)
