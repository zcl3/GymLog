package com.personal.gymlog

import android.app.Application
import com.personal.gymlog.data.local.AppDatabase
import com.personal.gymlog.data.repository.GymLogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GymLogApplication : Application() {
    val database by lazy { AppDatabase.create(this) }
    val repository by lazy { GymLogRepository(database) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            repository.seedBuiltInExercises()
        }
    }
}
