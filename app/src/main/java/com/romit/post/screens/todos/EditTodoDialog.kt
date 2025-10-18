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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.romit.post.data.model.Task
import com.romit.post.viewmodels.todoscreen.TodoScreenViewModel


@Composable
fun EditTodoDialog(
    isEditInProgress: Boolean,
    task: Task,
    viewModel: TodoScreenViewModel,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(task.title) }
    var text by remember { mutableStateOf(task.text) }

    Dialog(onDismissRequest = { viewModel.onDialogDismissed() }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Edit Todo",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

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
                    onClick = { viewModel.deleteTodo(task.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = !isEditInProgress,
                    modifier = modifier
                ) {
                    Text("Delete Todo")
                }

                Button(
                    onClick = { viewModel.updateTodo(task.id, title, text) },
                    enabled = title.isNotBlank() && !isEditInProgress,
                    modifier = modifier
                ) {
                    if (isEditInProgress) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Update Todo")
                    }
                }
            }
        }
    }
}