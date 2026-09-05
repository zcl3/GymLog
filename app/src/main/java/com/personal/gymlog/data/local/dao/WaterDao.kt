package com.personal.gymlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.personal.gymlog.data.local.entity.WaterEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Query("SELECT * FROM WaterEntry WHERE date = :date ORDER BY recordedAt DESC") fun observeDate(date: String): Flow<List<WaterEntry>>
    @Insert suspend fun insert(entry: WaterEntry): Long
    @Query("DELETE FROM WaterEntry WHERE id = :id") suspend fun delete(id: Long)
}
