package com.personal.gymlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.personal.gymlog.data.local.entity.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM Exercise WHERE isArchived = 0 ORDER BY bodyPart, name") fun observeActive(): Flow<List<Exercise>>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertAll(exercises: List<Exercise>)
    @Query("SELECT id FROM Exercise WHERE seedKey = :seedKey LIMIT 1") suspend fun findIdBySeedKey(seedKey: String): Long?
}
