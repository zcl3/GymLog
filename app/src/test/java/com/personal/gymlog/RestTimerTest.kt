package com.personal.gymlog

import com.personal.gymlog.feature.workout.formatRestTime
import org.junit.Assert.assertEquals
import org.junit.Test

class RestTimerTest {
    @Test fun formatsSecondsAsMinutesAndSeconds() {
        assertEquals("01:30", formatRestTime(90))
        assertEquals("00:05", formatRestTime(5))
        assertEquals("00:00", formatRestTime(-1))
    }
}
