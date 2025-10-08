package com.romit.post.screens.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.romit.post.data.model.Task
import com.romit.post.viewmodels.todoscreen.TodoScreenViewModel

// In the same file as AddTodoDialog

@Composable
fun EditTodoDialog(
    isEditInProgress: Boolean,
    task: Task, // The task to be edited
    viewModel: TodoScreenViewModel,
    modifier: Modifier = Modifier
) {
    // Use the passed-in task's data to pre-fill the state
    var title by remember { mutableStateOf(task.title) }
    var text by remember { mutableStateOf(task.text) }

    Dialog(onDismissRequest = { viewModel.onDialogDismissed() }) {
        Surface(
            Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Edit Todo")

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 3,
                    modifier = modifier
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Text") },
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 3,
                    modifier = modifier
                )
                Button(
                    // 2. Call the ViewModel function directly
                    onClick = { viewModel.deleteTodo(task.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), // Better styling for delete
                    modifier = modifier
                ) {
                    Text("Delete Todo")
                }

                // --- UPDATE BUTTON ---
                Button(
                    onClick = {
                        // 3. CRITICAL FIX: Call the ViewModel and DO NOT call onDismiss() here.
                        // The ViewModel's event flow will handle dismissing the dialog
                        // only after the update is successful.
                        viewModel.updateTodo(task.id, title, text)
                    },
                    enabled = title.isNotBlank(),
                    modifier = modifier
                ) {
                    // 4. Read the loading state directly from the UI state
                    if (isEditInProgress) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Update Todo")
                    }
                }
            }
        }

    }
}