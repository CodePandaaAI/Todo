package com.romit.post.screens.todos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.romit.post.R
import com.romit.post.viewmodels.todoscreen.TodoScreenViewModel

@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    todoViewModel: TodoScreenViewModel
) {
    val uiState by todoViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        todoViewModel.eventFlow.collect {
            todoViewModel.onDialogDismissed()
        }
    }

    if (uiState.showAddTodoDialog) {
        AddTodoDialog(viewModel = todoViewModel)
    }

    if (uiState.showEditTodoDialog && uiState.taskToEdit != null) {
        EditTodoDialog(
            isEditInProgress = uiState.isEditInProgress,
            task = uiState.taskToEdit!!,
            viewModel = todoViewModel,
            modifier = Modifier.fillMaxWidth()
        )
    }

    when {
        uiState.isLoading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage != null -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.errorMessage!!)
            }
        }

        uiState.tasks.isEmpty() -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.no_todo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .clip(CircleShape)
                )
            }
        }

        else -> {
            LazyVerticalStaggeredGrid(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.tasks, key = { it.id }) { task ->
                    TodoCard(
                        modifier = Modifier,
                        task = task,
                        onClick = { todoViewModel.onEditTaskClicked(it) }
                    )
                }
            }
        }
    }
}