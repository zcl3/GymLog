package com.personal.gymlog.data.repository

import com.personal.gymlog.data.local.AppDatabase
import com.personal.gymlog.data.local.entity.Exercise
import com.personal.gymlog.data.local.seed.BuiltInExerciseSeed
import kotlinx.coroutines.flow.Flow

class GymLogRepository(private val database: AppDatabase) {
    fun observeExercises(): Flow<List<Exercise>> = database.exerciseDao().observeActive()
    suspend fun seedBuiltInExercises() = BuiltInExerciseSeed.ensureSeeded(database.exerciseDao())
}
