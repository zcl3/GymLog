package com.personal.gymlog.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["seedKey"], unique = true), Index("name")])
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val seedKey: String? = null,
    val name: String,
    val bodyPart: String,
    val isBuiltIn: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = createdAt,
)

@Entity(indices = [Index("trainingDate"), Index("status")])
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val status: String = "IN_PROGRESS",
    val startedAt: Long,
    val endedAt: Long? = null,
    val trainingDate: String,
    val zoneId: String,
)

@Entity(
    indices = [Index(value = ["sessionId", "position"]), Index("exerciseId")],
    foreignKeys = [ForeignKey(entity = WorkoutSession::class, parentColumns = ["id"], childColumns = ["sessionId"], onDelete = ForeignKey.CASCADE), ForeignKey(entity = Exercise::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = ForeignKey.RESTRICT)],
)
data class WorkoutExercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val exerciseId: Long,
    val nameSnapshot: String,
    val bodyPartSnapshot: String,
    val position: Int,
)

@Entity(
    indices = [Index(value = ["workoutExerciseId", "position"])],
    foreignKeys = [ForeignKey(entity = WorkoutExercise::class, parentColumns = ["id"], childColumns = ["workoutExerciseId"], onDelete = ForeignKey.CASCADE)],
)
data class SetRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutExerciseId: Long,
    val position: Int,
    val weightGrams: Int,
    val reps: Int,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
)

@Entity
data class WorkoutTemplate(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String, val createdAt: Long = System.currentTimeMillis(), val updatedAt: Long = createdAt)

@Entity(
    indices = [Index(value = ["templateId", "position"]), Index("exerciseId")],
    foreignKeys = [ForeignKey(entity = WorkoutTemplate::class, parentColumns = ["id"], childColumns = ["templateId"], onDelete = ForeignKey.CASCADE), ForeignKey(entity = Exercise::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = ForeignKey.RESTRICT)],
)
data class WorkoutTemplateExercise(@PrimaryKey(autoGenerate = true) val id: Long = 0, val templateId: Long, val exerciseId: Long, val position: Int)

@Entity(indices = [Index(value = ["date", "mealType"]), Index("recordedAt")])
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val recordedAt: Long,
    val zoneId: String,
    val mealType: String,
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val caloriesKcal: Double? = null,
    val proteinGrams: Double? = null,
    val carbsGrams: Double? = null,
    val fatGrams: Double? = null,
    val note: String? = null,
)

@Entity
data class FavoriteFood(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val caloriesKcal: Double? = null,
    val proteinGrams: Double? = null,
    val carbsGrams: Double? = null,
    val fatGrams: Double? = null,
    val note: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = createdAt,
)

@Entity(indices = [Index(value = ["date", "recordedAt"])])
data class WaterEntry(@PrimaryKey(autoGenerate = true) val id: Long = 0, val amountMl: Int, val recordedAt: Long, val date: String, val zoneId: String)
