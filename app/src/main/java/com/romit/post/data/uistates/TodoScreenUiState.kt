package com.romit.post.data.uistates

import com.romit.post.data.model.Task

data class TodoScreenUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
