package com.personal.gymlog

import com.personal.gymlog.core.calculation.trainingVolumeGrams
import com.personal.gymlog.core.calculation.waterProgressPercent
import org.junit.Assert.assertEquals
import org.junit.Test

class TrainingCalculationsTest {
    @Test fun completedSetVolumeUsesWeightTimesReps() {
        assertEquals(800_000L, trainingVolumeGrams(80_000, 10, true))
        assertEquals(0L, trainingVolumeGrams(80_000, 10, false))
    }

    @Test fun waterProgressHandlesInvalidGoalAndOverflow() {
        assertEquals(70, waterProgressPercent(1750, 2500))
        assertEquals(120, waterProgressPercent(3000, 2500))
        assertEquals(0, waterProgressPercent(500, 0))
    }
}
