package com.personal.gymlog

import com.personal.gymlog.core.calculation.trainingVolumeGrams
import com.personal.gymlog.core.calculation.waterProgressPercent
import com.personal.gymlog.data.local.entity.SetRecord
import com.personal.gymlog.data.local.entity.WorkoutExercise
import com.personal.gymlog.data.local.entity.WorkoutSession
import com.personal.gymlog.data.local.relation.WorkoutDetails
import com.personal.gymlog.data.local.relation.WorkoutExerciseDetails
import com.personal.gymlog.feature.workout.workoutSummary
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TrainingCalculationsTest {
    @Test fun workoutSummaryIncludesActionWhenNoSetIsCompleted() {
        val session = WorkoutSession(id = 1, name = "训练", startedAt = 0, trainingDate = "2026-01-02", zoneId = "UTC")
        val exercise = WorkoutExercise(id = 2, sessionId = 1, exerciseId = 3, nameSnapshot = "卧推", bodyPartSnapshot = "胸部", position = 0)
        val set = SetRecord(id = 4, workoutExerciseId = 2, position = 0, weightGrams = 60_000, reps = 8)

        val summary = workoutSummary(WorkoutDetails(session, listOf(WorkoutExerciseDetails(exercise, listOf(set)))))

        assertTrue(summary.contains("卧推"))
        assertTrue(summary.contains("60 kg × 8"))
    }

    @Test fun completedSetVolumeUsesWeightTimesReps() {
        assertEquals(800_000L, trainingVolumeGrams(80_000, 10, true))
        assertEquals(0L, trainingVolumeGrams(80_000, 10, false))
    }

    @Test fun waterProgressHandlesInvalidGoalAndOverflow() {
        assertEquals(70, waterProgressPercent(1750, 2500))
        assertEquals(120, waterProgressPercent(3000, 2500))
        assertEquals(0, waterProgressPercent(500, 0))
    }
}
