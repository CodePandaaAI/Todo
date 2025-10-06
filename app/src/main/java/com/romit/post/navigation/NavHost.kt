package com.romit.post.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.romit.post.screens.Screen
import com.romit.post.screens.TodoScreen

@Composable
fun NavHost() {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(modifier = Modifier.padding(innerPadding),navController = navController, startDestination = Screen.TodoScreen){
            composable<Screen.TodoScreen> {
                TodoScreen()
            }
        }
    }
}

