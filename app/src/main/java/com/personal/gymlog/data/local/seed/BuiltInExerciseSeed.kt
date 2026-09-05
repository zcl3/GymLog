package com.personal.gymlog.data.local.seed

import com.personal.gymlog.data.local.dao.ExerciseDao
import com.personal.gymlog.data.local.entity.Exercise

object BuiltInExerciseSeed {
    private val exercises = listOf(
        Exercise(seedKey = "chest_barbell_bench", name = "杠铃卧推", bodyPart = "胸", isBuiltIn = true),
        Exercise(seedKey = "chest_dumbbell_bench", name = "哑铃卧推", bodyPart = "胸", isBuiltIn = true),
        Exercise(seedKey = "back_pull_up", name = "引体向上", bodyPart = "背", isBuiltIn = true),
        Exercise(seedKey = "back_lat_pulldown", name = "高位下拉", bodyPart = "背", isBuiltIn = true),
        Exercise(seedKey = "legs_squat", name = "深蹲", bodyPart = "腿", isBuiltIn = true),
        Exercise(seedKey = "legs_press", name = "腿举", bodyPart = "腿", isBuiltIn = true),
        Exercise(seedKey = "shoulder_press", name = "肩推", bodyPart = "肩", isBuiltIn = true),
        Exercise(seedKey = "arms_curl", name = "杠铃弯举", bodyPart = "二头", isBuiltIn = true),
        Exercise(seedKey = "arms_pushdown", name = "绳索下压", bodyPart = "三头", isBuiltIn = true),
        Exercise(seedKey = "core_plank", name = "平板支撑", bodyPart = "核心", isBuiltIn = true),
    )

    suspend fun ensureSeeded(dao: ExerciseDao) = dao.insertAll(exercises)
}
