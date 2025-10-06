package com.romit.post.viewmodels

import androidx.lifecycle.ViewModel
import com.romit.post.data.Task

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class TodoScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<List<Task>>(emptyList())
    val uiState = _uiState.asStateFlow()

    fun addTodo(title: String, text: String) {
        val newTodo =
            Task(
                id = UUID.randomUUID().toString(),
                title = title,
                text = text,
                isCompleted = false
            )
        _uiState.update { currentList ->
            currentList + newTodo
        }
    }
}