package com.personal.gymlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.personal.gymlog.data.local.entity.FoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM FoodEntry WHERE date = :date ORDER BY recordedAt DESC") fun observeDate(date: String): Flow<List<FoodEntry>>
    @Insert suspend fun insert(entry: FoodEntry): Long
    @Query("DELETE FROM FoodEntry WHERE id = :id") suspend fun delete(id: Long)
}
