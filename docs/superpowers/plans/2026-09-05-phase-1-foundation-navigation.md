# Phase 1 Foundation and Navigation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the Compose greeting with a maintainable GymLog shell containing five bottom-navigation destinations and a More screen for secondary destinations.

**Architecture:** Keep a single Android app module. Navigation owns route definitions and the NavHost; each destination owns a small screen composable; MainActivity only wires the theme and app shell.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Navigation Compose, existing project versions.

**Spec:** Phase 1 design confirmed in conversation and source request attachment.

## Global Constraints

- Preserve the existing Gradle, AGP, Kotlin, Compose versions and repository mirrors.
- Keep the first version offline and Chinese-first.
- Do not introduce Room, DataStore, WorkManager, or unrelated third-party libraries in Phase 1.
- Finish with `gradlew.bat assembleDebug` and the existing unit tests passing.

---

### Task 1: Add navigation dependency

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`

- [ ] Add the AndroidX Navigation Compose library version and implementation alias.
- [ ] Keep all existing versions unchanged.
- [ ] Resolve the dependency with a Gradle compile check.

### Task 2: Create app navigation shell

**Files:**
- Create: `app/src/main/java/com/personal/gymlog/navigation/AppDestination.kt`
- Create: `app/src/main/java/com/personal/gymlog/navigation/GymLogApp.kt`
- Create: `app/src/main/java/com/personal/gymlog/ui/components/GymLogBottomBar.kt`
- Modify: `app/src/main/java/com/personal/gymlog/MainActivity.kt`

- [ ] Define stable routes for Home, Workout, Nutrition, Water, More, Statistics, Exercises, Templates, and Settings.
- [ ] Render a Material 3 `Scaffold` with a bottom bar only for the five primary destinations.
- [ ] Configure `NavHost` with `saveState`, `restoreState`, and single-top bottom navigation behavior.
- [ ] Apply window insets through the Scaffold content padding.

### Task 3: Add Phase 1 screens

**Files:**
- Create: `app/src/main/java/com/personal/gymlog/feature/home/HomeScreen.kt`
- Create: `app/src/main/java/com/personal/gymlog/feature/workout/WorkoutScreen.kt`
- Create: `app/src/main/java/com/personal/gymlog/feature/nutrition/NutritionScreen.kt`
- Create: `app/src/main/java/com/personal/gymlog/feature/water/WaterScreen.kt`
- Create: `app/src/main/java/com/personal/gymlog/feature/more/MoreScreen.kt`

- [ ] Use large, clear Chinese labels and empty-state cards.
- [ ] Provide Phase 1 quick-entry buttons on Home without persistence.
- [ ] Make More entries navigate to placeholder Statistics, Exercise Library, Templates, and Settings screens.
- [ ] Keep placeholder secondary screens small and clearly marked as coming in later phases.

### Task 4: Verify and commit

- [ ] Run `gradlew.bat testDebugUnitTest`.
- [ ] Run `gradlew.bat assembleDebug`.
- [ ] Inspect the diff and ensure generated files and local configuration are ignored.
- [ ] Commit with `feat: add phase 1 app shell and navigation`.
- [ ] Push `main` to `origin`.
