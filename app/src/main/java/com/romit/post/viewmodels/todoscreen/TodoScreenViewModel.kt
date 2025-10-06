package com.romit.post.viewmodels.todoscreen

import android.R.attr.text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.romit.post.data.model.Task
import com.romit.post.data.uistates.TodoScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TodoScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TodoScreenUiState())
    val uiState = _uiState.asStateFlow()

    // Reference to firestore database
    private val firestore = Firebase.firestore

    private val currentUser = Firebase.auth.currentUser

    init {
        listenForTodoUpdates()
    }

    fun addTodo(title: String, text: String, onDismiss: () -> Unit) {
        _uiState.update { it.copy(errorMessage = null) }
        viewModelScope.launch {
            currentUser?.let { user ->
                val newTodo = Task(
                    title = title,
                    text = text,
                    isCompleted = false,
                    userId = user.uid
                )
                try {
                    val newTasks = uiState.value.tasks + newTodo
                    _uiState.update { currentState ->
                        currentState.copy(tasks = newTasks, errorMessage = null)
                    }
                    firestore.collection("todos").add(newTodo).await()
                    onDismiss()
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            }
        }
    }

    private fun listenForTodoUpdates() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            currentUser?.let { user ->
                firestore.collection("todos")
                    .whereEqualTo("userId", user.uid)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            _uiState.update { it.copy(isLoading = false,errorMessage = error.message) }
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val tasks = snapshot.toObjects(Task::class.java)
                            _uiState.update { currentState ->
                                currentState.copy(
                                    tasks = tasks,
                                    isLoading = false,
                                    errorMessage = null
                                )
                            }
                        }
                    }
            }
        }
    }
}