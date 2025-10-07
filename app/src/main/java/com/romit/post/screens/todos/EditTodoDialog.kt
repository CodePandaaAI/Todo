package com.romit.post.screens.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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

// In the same file as AddTodoDialog

@Composable
fun EditTodoDialog(
    task: Task, // The task to be edited
    onUpdateTodo: (taskId: String, title: String, text: String) -> Unit,
    isEditing: Boolean,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Use the passed-in task's data to pre-fill the state
    var title by remember { mutableStateOf(task.title) }
    var text by remember { mutableStateOf(task.text) }

    Dialog(onDismissRequest = { onDismiss() }) {
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
                    onClick = { onDelete() },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task"
                    )
                    Text("Delete Todo")
                }

                Button(
                    onClick = {
                        // Pass the task's original ID back
                        onUpdateTodo(task.id, title, text)
                        onDismiss()
                    }, enabled = !title.isBlank(),
                    modifier = modifier
                ) {
                    if (isEditing) {
                        CircularProgressIndicator()
                    } else Text("Update Todo")
                }
            }
        }

    }
}