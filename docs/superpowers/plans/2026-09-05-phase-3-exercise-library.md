# Phase 3 Exercise Library Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make the Exercise Library a usable offline feature with seeded exercises, filtering, search, and custom exercise management.

**Architecture:** The DAO exposes one Flow of active exercises and simple write operations. A small pure filter function keeps search and body-part behavior testable. The screen collects the repository-backed ViewModel state and uses dialogs for quick custom exercise creation.

**Tech Stack:** Kotlin, Room, Compose Material 3, ViewModel, Coroutines Flow.

**Spec:** Phase 3 design confirmed in conversation and source request attachment.

## Global Constraints

- Preserve existing build versions and repository mirrors.
- Built-in exercises cannot be edited or deleted; custom exercises are archived on delete.
- Keep the feature offline and Chinese-first.

---

### Task 1: Exercise filtering and DAO operations

**Files:** `data/local/dao/ExerciseDao.kt`, `feature/exercise/ExerciseFilter.kt`, tests.

- [ ] Add insert, update, archive, and lookup operations.
- [ ] Add a pure case-insensitive filter by body part and name.
- [ ] Write and run failing filter tests, then implement them.

### Task 2: Application data container

**Files:** `GymLogApplication.kt`, `MainActivity.kt`, `navigation/GymLogApp.kt`.

- [ ] Create one process-scoped Room database and repository.
- [ ] Seed built-in exercises on application startup.
- [ ] Pass the repository into the exercise destination.

### Task 3: Exercise library UI and ViewModel

**Files:** `feature/exercise/ExerciseLibraryViewModel.kt`, `ExerciseLibraryScreen.kt`, navigation routes.

- [ ] Display search, body-part chips, and grouped exercise rows.
- [ ] Add a custom exercise dialog with name and body-part validation.
- [ ] Allow editing and archiving custom exercises while preserving built-ins.

### Task 4: Verify and publish

- [ ] Run unit tests and `assembleDebug`.
- [ ] Commit `feat: add phase 3 exercise library`.
- [ ] Push all local commits to `origin/main` when network is available.
