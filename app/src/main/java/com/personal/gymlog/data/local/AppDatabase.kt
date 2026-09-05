package com.personal.gymlog.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.personal.gymlog.data.local.dao.ExerciseDao
import com.personal.gymlog.data.local.dao.WorkoutDao
import com.personal.gymlog.data.local.dao.TemplateDao
import com.personal.gymlog.data.local.dao.FoodDao
import com.personal.gymlog.data.local.dao.WaterDao
import com.personal.gymlog.data.local.entity.Exercise
import com.personal.gymlog.data.local.entity.FavoriteFood
import com.personal.gymlog.data.local.entity.FoodEntry
import com.personal.gymlog.data.local.entity.SetRecord
import com.personal.gymlog.data.local.entity.WaterEntry
import com.personal.gymlog.data.local.entity.WorkoutExercise
import com.personal.gymlog.data.local.entity.WorkoutSession
import com.personal.gymlog.data.local.entity.WorkoutTemplate
import com.personal.gymlog.data.local.entity.WorkoutTemplateExercise

@Database(entities = [Exercise::class, WorkoutSession::class, WorkoutExercise::class, SetRecord::class, WorkoutTemplate::class, WorkoutTemplateExercise::class, FoodEntry::class, FavoriteFood::class, WaterEntry::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun templateDao(): TemplateDao
    abstract fun foodDao(): FoodDao
    abstract fun waterDao(): WaterDao

    companion object {
        fun create(context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "gymlog.db").build()
    }
}
