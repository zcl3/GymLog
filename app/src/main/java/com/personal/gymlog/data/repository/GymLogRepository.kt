package com.personal.gymlog.data.repository

import com.personal.gymlog.data.local.AppDatabase
import com.personal.gymlog.data.local.entity.Exercise
import com.personal.gymlog.data.local.seed.BuiltInExerciseSeed
import kotlinx.coroutines.flow.Flow

class GymLogRepository(private val database: AppDatabase) {
    fun observeExercises(): Flow<List<Exercise>> = database.exerciseDao().observeActive()
    suspend fun addExercise(name: String, bodyPart: String): Long = database.exerciseDao().insert(Exercise(name = name, bodyPart = bodyPart))
    suspend fun updateExercise(exercise: Exercise) {
        if (!exercise.isBuiltIn) database.exerciseDao().update(exercise)
    }
    suspend fun archiveExercise(id: Long) = database.exerciseDao().archive(id)
    suspend fun seedBuiltInExercises() = BuiltInExerciseSeed.ensureSeeded(database.exerciseDao())
}
