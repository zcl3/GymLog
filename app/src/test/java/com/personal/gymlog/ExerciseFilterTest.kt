package com.personal.gymlog

import com.personal.gymlog.data.local.entity.Exercise
import com.personal.gymlog.feature.exercise.filterExercises
import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseFilterTest {
    private val exercises = listOf(
        Exercise(id = 1, name = "杠铃卧推", bodyPart = "胸", isBuiltIn = true),
        Exercise(id = 2, name = "深蹲", bodyPart = "腿", isBuiltIn = true),
        Exercise(id = 3, name = "哑铃卧推", bodyPart = "胸", isBuiltIn = true),
    )

    @Test fun filterMatchesBodyPartAndName() {
        assertEquals(listOf(1L, 3L), filterExercises(exercises, "卧推", "胸").map { it.id })
        assertEquals(listOf(2L), filterExercises(exercises, "", "腿").map { it.id })
    }
}
