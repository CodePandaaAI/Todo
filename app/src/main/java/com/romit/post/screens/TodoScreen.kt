package com.romit.post.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.romit.post.R
import com.romit.post.viewmodels.TodoScreenViewModel

@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    todoViewModel: TodoScreenViewModel = viewModel()
) {
    val uiState by todoViewModel.uiState.collectAsState()

    if (showDialog) {
        AddTodoDialog(
            onSaveTodo = { title, text -> todoViewModel.addTodo(title, text) },
            onDismiss = { onDismissDialog() })
    }

    if (uiState.isEmpty()) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.no_todo),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clip(
                        CircleShape
                    )
            )
        }
        return
    }
    LazyVerticalGrid(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uiState) { task ->
            TodoCard(task.title, task.text, modifier = Modifier)
        }
    }
}

@Composable
fun TodoCard(title: String, text: String, modifier: Modifier) {
    Surface(
        modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(24.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun AddTodoDialog(onSaveTodo: (title: String, text: String) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
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

                OutlinedButton(onClick = {
                    onSaveTodo(title, text)
                    onDismiss()
                }
                ) {
                    Text("Save Todo")
                }
            }
        }
    }
}