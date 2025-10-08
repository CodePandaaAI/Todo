package com.romit.post.viewmodels.todoscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.romit.post.data.model.Task
import com.romit.post.data.model.TodoResult
import com.romit.post.data.repositories.TodoRepositoryImpl
import com.romit.post.data.uistates.TodoScreenUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TodoScreenUiState())
    val uiState = _uiState.asStateFlow()

    val todoRepository = TodoRepositoryImpl()

    private val currentUser = Firebase.auth.currentUser

    // --- ADD A SHARED FLOW FOR ONE-TIME EVENTS ---
    private val _eventFlow = MutableSharedFlow<Unit>()
    val eventFlow = _eventFlow.asSharedFlow()
    // ---------------------------------------------

    init {
        listenForTodoUpdates()
    }

    fun addTodo(title: String, text: String) {
        // We no longer need the onDismiss lambda
        _uiState.update { it.copy(isEditInProgress = true) } // Show a loading indicator
        viewModelScope.launch {
            currentUser?.let { user ->
                val newTodo = Task(
                    title = title,
                    text = text,
                    isCompleted = false,
                    userId = user.uid
                )
                try {
                    // Just call the repository. The real-time listener will handle the UI update.
                    todoRepository.addTodo(newTodo)
                    // On success, emit an event to tell the UI to close the dialog
                    _eventFlow.emit(Unit)
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = e.message) }
                } finally {
                    // Ensure the loading indicator is turned off
                    _uiState.update { it.copy(isEditInProgress = false) }
                }
            }
        }
    }

    fun updateTodo(taskId: String, newTitle: String, newText: String) {
        // We can't update a task without its ID
        if (taskId.isBlank()) {
            // Error: Task ID is blank, cannot update.
            return
        }
        _uiState.update { it.copy(isEditInProgress = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                todoRepository.updateTodo(taskId, newTitle, newText)
                _uiState.update { it.copy(isEditInProgress = false) }
            } catch (e: Exception) {
            }
        }
    }

    fun deleteTodo(taskId: String) {
        if (taskId.isBlank()) {
            // Error: Task ID is blank, cannot delete.
            return
        }
        _uiState.update { it.copy(isEditInProgress = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                // Get a reference to the document and delete it
                todoRepository.deleteTodo(taskId)
                _uiState.update { it.copy(isEditInProgress = false) }
            } catch (e: Exception) {

            }
        }
    }

    // Called when the FAB is clicked
    fun onAddTaskClicked() {
        _uiState.update { it.copy(showAddTodoDialog = true) }
    }
    // Called when the user clicks on a card
    fun onEditTaskClicked(task: Task) {
        _uiState.update { it.copy(showEditTodoDialog = true, taskToEdit = task) }
    }

    // Called by BOTH dialogs when they are dismissed
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
            // Telling the UI we are loading
            _uiState.update { it.copy(isLoading = true) }

            // Collecting the stream of results from the repository
            todoRepository.getTodos().collect { result ->
                // Checking the type of the incoming 'result'
                when (result) {
                    is TodoResult.Success -> {
                        // Inside this block, Kotlin now KNOWS 'result' is a Success type,
                        // so we can safely access the 'result.data' property.
                        _uiState.update {
                            it.copy(
                                tasks = result.data,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is TodoResult.Failure -> {
                        // Inside this block, Kotlin knows 'result' is a Failure type,
                        // so we can safely access the 'result.exception' property.
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.exception.message
                            )
                        }
                    }
                    is TodoResult.Loading -> {
                        // This case handles the loading state if your repository emits it.
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }
}