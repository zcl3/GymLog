package com.personal.gymlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.personal.gymlog.data.local.entity.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM Exercise WHERE isArchived = 0 ORDER BY bodyPart, name") fun observeActive(): Flow<List<Exercise>>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertAll(exercises: List<Exercise>)
    @Insert suspend fun insert(exercise: Exercise): Long
    @Update suspend fun update(exercise: Exercise)
    @Query("UPDATE Exercise SET isArchived = 1, updatedAt = :updatedAt WHERE id = :id AND isBuiltIn = 0") suspend fun archive(id: Long, updatedAt: Long = System.currentTimeMillis())
    @Query("SELECT id FROM Exercise WHERE seedKey = :seedKey LIMIT 1") suspend fun findIdBySeedKey(seedKey: String): Long?
}
