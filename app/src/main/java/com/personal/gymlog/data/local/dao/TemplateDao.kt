package com.personal.gymlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.personal.gymlog.data.local.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Query("SELECT * FROM WorkoutTemplate ORDER BY updatedAt DESC") fun observeAll(): Flow<List<WorkoutTemplate>>
    @Insert suspend fun insert(template: WorkoutTemplate): Long
    @Query("DELETE FROM WorkoutTemplate WHERE id = :id") suspend fun delete(id: Long)
}
