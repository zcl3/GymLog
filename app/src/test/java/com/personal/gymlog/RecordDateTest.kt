package com.personal.gymlog

import com.personal.gymlog.data.settings.AppSettings
import com.personal.gymlog.data.settings.recordDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class RecordDateTest {
    @Test fun usesSelectedIsoDate() {
        assertEquals("2026-01-02", AppSettings(currentDate = "2026-01-02").recordDate())
    }

    @Test fun malformedSavedDateFallsBackToToday() {
        assertEquals(LocalDate.now().toString(), AppSettings(currentDate = "not-a-date").recordDate())
    }
}
