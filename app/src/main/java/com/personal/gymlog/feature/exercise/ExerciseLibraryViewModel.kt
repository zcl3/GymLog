package com.personal.gymlog.feature.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.personal.gymlog.data.local.entity.Exercise
import com.personal.gymlog.data.repository.GymLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ExerciseLibraryState(val exercises: List<Exercise> = emptyList(), val query: String = "", val bodyPart: String? = null)

class ExerciseLibraryViewModel(private val repository: GymLogRepository) : ViewModel() {
    private val query = MutableStateFlow("")
    private val bodyPart = MutableStateFlow<String?>(null)
    val state: StateFlow<ExerciseLibraryState> = combine(repository.observeExercises(), query, bodyPart) { exercises, text, part ->
        ExerciseLibraryState(filterExercises(exercises, text, part), text, part)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ExerciseLibraryState())

    fun setQuery(value: String) { query.value = value }
    fun setBodyPart(value: String?) { bodyPart.value = value }
    fun add(name: String, part: String) = viewModelScope.launch { repository.addExercise(name.trim(), part) }
    fun archive(exercise: Exercise) = viewModelScope.launch { if (!exercise.isBuiltIn) repository.archiveExercise(exercise.id) }

    class Factory(private val repository: GymLogRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = ExerciseLibraryViewModel(repository) as T
    }
}
