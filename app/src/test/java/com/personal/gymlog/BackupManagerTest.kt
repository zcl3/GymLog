package com.personal.gymlog

import com.personal.gymlog.data.backup.BackupManager
import com.personal.gymlog.data.local.entity.Exercise
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupManagerTest {
    @Test fun exportsSupportedVersionedJson() { assertTrue(BackupManager.isSupported(BackupManager.export(listOf(Exercise(name = "深蹲", bodyPart = "腿")), emptyList()))) }
}
