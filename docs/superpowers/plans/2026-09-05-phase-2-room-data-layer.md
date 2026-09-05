# Phase 2 Room Data Layer Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add the offline Room and DataStore foundation needed by later GymLog features without changing the existing navigation behavior.

**Architecture:** Room stores normalized workout, exercise, template, nutrition, and water entities. DAOs expose Flow and transactional operations through a repository. DataStore stores only user preferences and is exposed through a typed settings repository.

**Tech Stack:** Kotlin, Room, KSP, DataStore Preferences, Coroutines Flow, existing Compose stack.

**Spec:** Phase 2 design confirmed in conversation and source request attachment.

## Global Constraints

- Preserve existing Gradle, AGP, Kotlin, Compose versions and repository mirrors.
- Keep all data local and offline.
- Use stable string codes for enums and integer grams/milliliters for persisted measurements.
- Keep Room schema export enabled for future migrations.

---

### Task 1: Configure Room and DataStore dependencies

**Files:** `gradle/libs.versions.toml`, `build.gradle.kts`, `app/build.gradle.kts`

- [ ] Add AGP-compatible KSP, Room, and DataStore aliases.
- [ ] Apply KSP only to the app module and configure Room schema output.
- [ ] Run a compile check before adding entities.

### Task 2: Add entities and relations

**Files:** `app/src/main/java/com/personal/gymlog/data/local/entity/*.kt`, `AppDatabase.kt`

- [ ] Define the nine entities with explicit foreign keys, indices, stable codes, timestamps, and snapshot fields.
- [ ] Define `WorkoutDetails` relation projections and register all entities in `AppDatabase` version 1.

### Task 3: Add DAOs and repository

**Files:** `data/local/dao/*.kt`, `data/repository/GymLogRepository.kt`, `data/local/seed/BuiltInExerciseSeed.kt`

- [ ] Add CRUD/Flow queries for exercises, sessions, sets, foods, water, and templates.
- [ ] Add a transaction that seeds built-in exercises idempotently.
- [ ] Add focused pure calculation tests for workout volume and water percentage.

### Task 4: Add typed DataStore settings

**Files:** `data/settings/AppSettings.kt`, `data/settings/SettingsRepository.kt`

- [ ] Expose unit, rest seconds, water goal, and reminder settings with defaults.
- [ ] Keep the preferences API independent from Room.

### Task 5: Verify and publish

- [ ] Run unit tests and `assembleDebug`.
- [ ] Inspect schema output and Git diff.
- [ ] Commit `feat: add phase 2 offline data layer` and push `main`.
