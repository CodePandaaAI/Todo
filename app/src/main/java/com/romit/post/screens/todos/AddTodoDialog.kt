package com.romit.post.screens.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

@Composable
fun AddTodoDialog(onSaveTodo: (title: String, text: String) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var isSaveButtonVisible by remember { mutableStateOf(true) }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Add Todo")

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") })

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Text") })

                OutlinedButton(
                    onClick = {
                        onSaveTodo(title, text)
                        isSaveButtonVisible = !isSaveButtonVisible
                    }, enabled = isSaveButtonVisible
                ) {
                    Text("Save Todo")
                }
            }
        }
    }
}