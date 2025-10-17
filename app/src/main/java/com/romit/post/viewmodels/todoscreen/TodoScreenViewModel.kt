package com.romit.post.viewmodels.todoscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romit.post.data.model.Task
import com.romit.post.data.model.TodoResult
import com.romit.post.data.repositories.TodoRepository
import com.romit.post.data.uistates.TodoScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoScreenViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Unit>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        listenForTodoUpdates()
    }

    fun addTodo(title: String, text: String) {
        _uiState.update { it.copy(isEditInProgress = true) }

        viewModelScope.launch {
            try {
                val newTodo = Task(
                    title = title,
                    text = text,
                    isCompleted = false
                    // userId is set by repository
                )
                todoRepository.addTodo(newTodo)
                _eventFlow.emit(Unit)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            } finally {
                _uiState.update { it.copy(isEditInProgress = false) }
            }
        }
    }

    fun updateTodo(taskId: String, newTitle: String, newText: String) {
        if (taskId.isBlank()) return

        _uiState.update { it.copy(isEditInProgress = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                todoRepository.updateTodo(taskId, newTitle, newText)
                _uiState.update { it.copy(isEditInProgress = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isEditInProgress = false, errorMessage = e.message)
                }
            }
        }
    }

    fun deleteTodo(taskId: String) {
        if (taskId.isBlank()) return

        _uiState.update { it.copy(isEditInProgress = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                todoRepository.deleteTodo(taskId)
                _uiState.update { it.copy(isEditInProgress = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isEditInProgress = false, errorMessage = e.message)
                }
            }
        }
    }

    fun onAddTaskClicked() {
        _uiState.update { it.copy(showAddTodoDialog = true) }
    }

    fun onEditTaskClicked(task: Task) {
        _uiState.update { it.copy(showEditTodoDialog = true, taskToEdit = task) }
    }

    fun onDialogDismissed() {
        _uiState.update {
            it.copy(
                showAddTodoDialog = false,
                showEditTodoDialog = false,
                taskToEdit = null
            )
        }
    }

    private fun listenForTodoUpdates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            todoRepository.getTodos().collect { result ->
                when (result) {
                    is TodoResult.Success -> {
                        _uiState.update {
                            it.copy(
                                tasks = result.data,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is TodoResult.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.exception.message
                            )
                        }
                    }
                    is TodoResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }
}