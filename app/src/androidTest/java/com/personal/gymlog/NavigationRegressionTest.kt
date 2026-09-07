package com.personal.gymlog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import com.personal.gymlog.data.local.entity.Exercise
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.Rule
import org.junit.Test

class NavigationRegressionTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun homeQuickEntryThenBottomHomeReturnsToDashboard() {
        compose.onAllNodesWithText("查看并记录")[0].performClick()
        compose.onNodeWithContentDescription("饮食", useUnmergedTree = true).performClick()
        compose.onNodeWithContentDescription("首页", useUnmergedTree = true).performClick()
        compose.onNodeWithText("今天").assertIsDisplayed()
    }

    @Test fun moreIsOpenedFromHomeActionAndIsNotABottomTab() {
        compose.onNodeWithContentDescription("更多设置").assertIsDisplayed()
        compose.onAllNodesWithContentDescription("更多", useUnmergedTree = true).assertCountEquals(0)
    }

    @Test fun moreExposesDateAndWaterGoalWithoutScrollingPastCalendar() {
        compose.onNodeWithContentDescription("更多设置").performClick()
        compose.onNodeWithText("记录日期").assertIsDisplayed()
        compose.onNodeWithText("每日饮水目标").assertIsDisplayed()
        compose.onNodeWithText("字体大小").assertIsDisplayed()
    }

    @Test fun foodDialogOffersTheFourMealChoices() {
        compose.onNodeWithContentDescription("饮食", useUnmergedTree = true).performClick()
        compose.onNodeWithText("添加食物").performClick()
        listOf("早餐", "午餐", "晚餐", "加餐").forEach { meal ->
            compose.onNodeWithText(meal).assertIsDisplayed()
        }
    }

    @Test fun customExerciseDialogOffersAbdomen() {
        compose.onNodeWithContentDescription("更多设置").performClick()
        compose.onNodeWithTag("more_list").performScrollToNode(hasText("动作库"))
        compose.onNodeWithText("动作库").performClick()
        compose.waitForIdle()
        compose.onNodeWithTag("exercise_list").performScrollToNode(hasText("新建自定义动作"))
        compose.onNodeWithText("新建自定义动作").performClick()
        compose.onNodeWithText("腹").assertIsDisplayed()
    }

    @Test fun selectedMealAndProteinAreShownAfterSavingFood() {
        compose.onNodeWithContentDescription("饮食", useUnmergedTree = true).performClick()
        compose.onNodeWithText("添加食物").performClick()
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("鸡胸肉")
        compose.onAllNodesWithText("午餐")[0].performClick()
        compose.onAllNodes(hasSetTextAction())[2].performTextInput("25")
        compose.onNodeWithText("保存").performClick()
        compose.onNodeWithText("午餐 · 鸡胸肉").assertIsDisplayed()
        compose.onNodeWithText("蛋白质 25 g").assertIsDisplayed()
    }

    @Test fun waterGoalCanBeChangedFromMore() {
        compose.onNodeWithContentDescription("更多设置").performClick()
        compose.onAllNodesWithText("修改")[0].performClick()
        compose.onNode(hasSetTextAction()).performTextClearance()
        compose.onNode(hasSetTextAction()).performTextInput("3000")
        compose.onNodeWithText("保存").performClick()
        compose.onNodeWithTag("more_list").performScrollToNode(hasText("3000 ml"))
        compose.onNodeWithText("3000 ml").assertIsDisplayed()
    }

    @Test fun historyOpensCompletedWorkoutDetails() {
        val repository = (compose.activity.application as GymLogApplication).repository
        runBlocking {
            val exerciseId = repository.addExercise("测试推举", "胸部")
            val exercise = repository.allExercises().first { it.id == exerciseId }
            val sessionId = repository.startWorkout("测试训练")
            val workoutExerciseId = repository.addWorkoutExercise(sessionId, exercise, 0)
            repository.addSet(workoutExerciseId, 0)
            val set = repository.sets(workoutExerciseId).single()
            repository.updateSet(set, weightGrams = 20_000, reps = 8, completed = true)
            repository.completeWorkout(sessionId)
        }
        compose.waitForIdle()
        compose.onNodeWithContentDescription("更多设置").performClick()
        compose.onNodeWithTag("more_list").performScrollToNode(hasText("训练历史"))
        compose.onNodeWithText("训练历史").performClick()
        compose.onNodeWithText("1 个动作 · 1 组已完成").performClick()
        compose.onNodeWithText("测试推举").assertIsDisplayed()
        compose.onNodeWithText("第 1 组 · 20 kg × 8").assertIsDisplayed()
    }

    @Test fun statisticsSummarizesCompletedTrainingVolume() {
        val repository = (compose.activity.application as GymLogApplication).repository
        val initialWorkouts = runBlocking { repository.observeHistoryDetails().first() }
        val expectedCount = initialWorkouts.size + 1
        val previousVolume = initialWorkouts.flatMap { it.exercises }.flatMap { it.sets }
            .filter { it.isCompleted }
            .sumOf { it.weightGrams / 1000.0 * it.reps }
        val expectedVolume = previousVolume + 160.0
        val expectedVolumeText = if (expectedVolume % 1.0 == 0.0) expectedVolume.toInt().toString() else "%.1f".format(expectedVolume)
        runBlocking {
            val exerciseId = repository.addExercise("统计测试动作", "腿部")
            val exercise = repository.allExercises().first { it.id == exerciseId }
            val sessionId = repository.startWorkout("统计测试")
            val workoutExerciseId = repository.addWorkoutExercise(sessionId, exercise, 0)
            repository.addSet(workoutExerciseId, 0)
            repository.updateSet(repository.sets(workoutExerciseId).single(), weightGrams = 20_000, reps = 8, completed = true)
            repository.completeWorkout(sessionId)
        }
        compose.waitForIdle()
        compose.onNodeWithContentDescription("更多设置").performClick()
        compose.onNodeWithTag("more_list").performScrollToNode(hasText("统计"))
        compose.onNodeWithText("统计").performClick()
        compose.onNodeWithText("完成训练").assertIsDisplayed()
        compose.onNodeWithText("$expectedCount 次").assertIsDisplayed()
        compose.onNodeWithText("累计训练量").assertIsDisplayed()
        compose.onNodeWithText("$expectedVolumeText kg").assertIsDisplayed()
    }

    @Test fun workoutHistoryCanDeleteACompletedTraining() {
        val repository = (compose.activity.application as GymLogApplication).repository
        val date = "2026-02-03"
        val sessionId = runBlocking {
            val exerciseId = repository.addExercise("待删除动作", "胸部")
            val exercise = repository.allExercises().first { it.id == exerciseId }
            val created = repository.startWorkout("待删除训练", date)
            val workoutExerciseId = repository.addWorkoutExercise(created, exercise, 0)
            repository.addSet(workoutExerciseId, 0)
            repository.completeWorkout(created)
            created
        }
        runBlocking { repository.deleteWorkout(sessionId) }
        val remaining = runBlocking { repository.observeWorkoutDetails(date).first() }
        assert(remaining.none { it.session.id == sessionId })
    }
}
