package com.personal.gymlog.core.calculation

fun trainingVolumeGrams(weightGrams: Int, reps: Int, completed: Boolean): Long =
    if (completed && weightGrams > 0 && reps > 0) weightGrams.toLong() * reps else 0L

fun waterProgressPercent(consumedMl: Int, goalMl: Int): Int =
    if (goalMl <= 0) 0 else (consumedMl * 100 / goalMl).coerceAtLeast(0)
