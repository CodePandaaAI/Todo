package com.romit.post.screens.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.romit.post.viewmodels.todoscreen.TodoScreenViewModel

@Composable
fun AddTodoDialog(
    viewModel: TodoScreenViewModel,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { viewModel.onDialogDismissed() }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Todo",
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
                    maxLines = 4,
                    modifier = modifier
                )

                Button(
                    onClick = { viewModel.addTodo(title, text) },
                    enabled = title.isNotBlank(),
                    modifier = modifier
                ) {
                    Text("Save Todo")
                }
            }
        }
    }
}

@Preview
@Composable
fun AddTodoDialogPreview() {
    AddTodoDialog(viewModel = viewModel())
}