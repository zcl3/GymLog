package com.personal.gymlog.data.repository

import com.personal.gymlog.data.local.AppDatabase
import com.personal.gymlog.data.local.entity.Exercise
import com.personal.gymlog.data.local.entity.SetRecord
import com.personal.gymlog.data.local.entity.WorkoutExercise
import com.personal.gymlog.data.local.entity.WorkoutSession
import com.personal.gymlog.data.local.entity.WorkoutTemplate
import com.personal.gymlog.data.local.entity.FoodEntry
import com.personal.gymlog.data.local.entity.WaterEntry
import com.personal.gymlog.data.local.relation.WorkoutDetails
import java.time.LocalDate
import java.time.ZoneId
import com.personal.gymlog.data.local.seed.BuiltInExerciseSeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GymLogRepository(private val database: AppDatabase) {
    fun observeExercises(): Flow<List<Exercise>> = database.exerciseDao().observeActive()
    suspend fun addExercise(name: String, bodyPart: String): Long = database.exerciseDao().insert(Exercise(name = name, bodyPart = bodyPart))
    suspend fun updateExercise(exercise: Exercise) {
        if (!exercise.isBuiltIn) database.exerciseDao().update(exercise)
    }
    suspend fun archiveExercise(id: Long) = database.exerciseDao().archive(id)
    suspend fun allExercises(): List<Exercise> = database.exerciseDao().getAllActive()
    suspend fun startWorkout(name: String, date: String = LocalDate.now().toString()): Long {
        val now = System.currentTimeMillis()
        return database.workoutDao().insertSession(WorkoutSession(name = name, startedAt = now, trainingDate = date, zoneId = ZoneId.systemDefault().id))
    }
    suspend fun inProgress(): WorkoutSession? = database.workoutDao().observeInProgress().first()
    suspend fun exercises(sessionId: Long): List<WorkoutExercise> = database.workoutDao().getExercises(sessionId)
    suspend fun workoutDetails(sessionId: Long) = database.workoutDao().getDetails(sessionId)
    suspend fun addWorkoutExercise(sessionId: Long, exercise: Exercise, position: Int): Long = database.workoutDao().insertExercise(WorkoutExercise(sessionId = sessionId, exerciseId = exercise.id, nameSnapshot = exercise.name, bodyPartSnapshot = exercise.bodyPart, position = position))
    suspend fun sets(workoutExerciseId: Long): List<SetRecord> = database.workoutDao().getSets(workoutExerciseId)
    suspend fun addSet(workoutExerciseId: Long, position: Int): Long = database.workoutDao().insertSet(SetRecord(workoutExerciseId = workoutExerciseId, position = position, weightGrams = 0, reps = 0))
    suspend fun updateSet(record: SetRecord, weightGrams: Int, reps: Int, completed: Boolean) = database.workoutDao().updateSet(record.id, weightGrams, reps, completed, if (completed) System.currentTimeMillis() else null)
    suspend fun completeWorkout(id: Long) = database.workoutDao().complete(id, System.currentTimeMillis())
    suspend fun deleteWorkout(id: Long) = database.workoutDao().deleteSession(id)
    fun observeHistory(): Flow<List<WorkoutSession>> = database.workoutDao().observeHistory()
    fun observeHistoryDetails(): Flow<List<WorkoutDetails>> = database.workoutDao().observeHistoryDetails()
    fun observeWorkouts(date: String): Flow<List<WorkoutSession>> = database.workoutDao().observeDate(date)
    fun observeWorkoutDetails(date: String): Flow<List<WorkoutDetails>> = database.workoutDao().observeDetailsForDate(date)
    fun observeTemplates(): Flow<List<WorkoutTemplate>> = database.templateDao().observeAll()
    suspend fun addTemplate(name: String) = database.templateDao().insert(WorkoutTemplate(name = name.trim()))
    suspend fun deleteTemplate(id: Long) = database.templateDao().delete(id)
    fun observeFoods(date: String = LocalDate.now().toString()): Flow<List<FoodEntry>> = database.foodDao().observeDate(date)
    suspend fun addFood(name: String, mealType: String, calories: Double?, protein: Double?, carbs: Double?, fat: Double?, date: String = LocalDate.now().toString()) {
        val now = System.currentTimeMillis()
        database.foodDao().insert(FoodEntry(date = date, recordedAt = now, zoneId = ZoneId.systemDefault().id, mealType = mealType, name = name.trim(), caloriesKcal = calories, proteinGrams = protein, carbsGrams = carbs, fatGrams = fat))
    }
    suspend fun updateFood(entry: FoodEntry, name: String, mealType: String, calories: Double?, protein: Double?, carbs: Double?, fat: Double?) =
        database.foodDao().update(entry.copy(name = name.trim(), mealType = mealType, caloriesKcal = calories, proteinGrams = protein, carbsGrams = carbs, fatGrams = fat))
    suspend fun deleteFood(id: Long) = database.foodDao().delete(id)
    fun observeWater(date: String = LocalDate.now().toString()): Flow<List<WaterEntry>> = database.waterDao().observeDate(date)
    suspend fun addWater(amountMl: Int, date: String = LocalDate.now().toString()) {
        require(amountMl in 1..10_000) { "单次饮水量请填写 1–10000 ml" }
        val now = System.currentTimeMillis()
        database.waterDao().insert(WaterEntry(amountMl = amountMl, recordedAt = now, date = date, zoneId = ZoneId.systemDefault().id))
    }
    suspend fun deleteWater(id: Long) = database.waterDao().delete(id)
    suspend fun seedBuiltInExercises() = BuiltInExerciseSeed.ensureSeeded(database.exerciseDao())
}
