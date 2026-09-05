package com.personal.gymlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.personal.gymlog.data.local.entity.SetRecord
import com.personal.gymlog.data.local.entity.WorkoutExercise
import com.personal.gymlog.data.local.entity.WorkoutSession
import com.personal.gymlog.data.local.relation.WorkoutDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert suspend fun insertSession(session: WorkoutSession): Long
    @Insert suspend fun insertExercise(exercise: WorkoutExercise): Long
    @Insert suspend fun insertSet(record: SetRecord): Long
    @Query("SELECT * FROM WorkoutSession WHERE status = 'IN_PROGRESS' ORDER BY startedAt DESC LIMIT 1") fun observeInProgress(): Flow<WorkoutSession?>
    @Query("SELECT * FROM WorkoutSession WHERE status = 'COMPLETED' ORDER BY trainingDate DESC, startedAt DESC") fun observeHistory(): Flow<List<WorkoutSession>>
    @Query("SELECT * FROM WorkoutSession WHERE trainingDate = :date ORDER BY startedAt DESC") fun observeDate(date: String): Flow<List<WorkoutSession>>
    @Query("SELECT * FROM WorkoutSession WHERE id = :id") suspend fun getSession(id: Long): WorkoutSession?
    @Query("SELECT * FROM WorkoutExercise WHERE sessionId = :sessionId ORDER BY position") suspend fun getExercises(sessionId: Long): List<WorkoutExercise>
    @Query("SELECT * FROM SetRecord WHERE workoutExerciseId = :workoutExerciseId ORDER BY position") suspend fun getSets(workoutExerciseId: Long): List<SetRecord>
    @Query("UPDATE WorkoutSession SET status = 'COMPLETED', endedAt = :endedAt WHERE id = :id") suspend fun complete(id: Long, endedAt: Long)
    @Query("UPDATE SetRecord SET weightGrams = :weightGrams, reps = :reps, isCompleted = :completed, completedAt = :completedAt WHERE id = :id") suspend fun updateSet(id: Long, weightGrams: Int, reps: Int, completed: Boolean, completedAt: Long?)
    @Transaction
    @Query("SELECT * FROM WorkoutSession WHERE id = :id")
    suspend fun getDetails(id: Long): WorkoutDetails?
}
