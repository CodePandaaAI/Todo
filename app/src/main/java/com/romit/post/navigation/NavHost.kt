package com.romit.post.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.romit.post.screens.Screen
import com.romit.post.screens.todos.TodoScreen
import com.romit.post.viewmodels.todoscreen.TodoScreenViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val todoViewModel: TodoScreenViewModel = hiltViewModel()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { todoViewModel.onAddTaskClicked() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Todo"
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screen.TodoScreen
        ) {
            composable<Screen.TodoScreen> {
                TodoScreen(todoViewModel = todoViewModel)
            }
        }
    }
}