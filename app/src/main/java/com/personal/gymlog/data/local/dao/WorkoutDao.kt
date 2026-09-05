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
    @Query("SELECT * FROM WorkoutSession WHERE id = :id") suspend fun getSession(id: Long): WorkoutSession?
    @Transaction
    @Query("SELECT * FROM WorkoutSession WHERE id = :id")
    suspend fun getDetails(id: Long): WorkoutDetails?
}
