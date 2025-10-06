package com.romit.post

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.romit.post.navigation.AppNavHost
import com.romit.post.screens.auth.AuthScreen
import com.romit.post.ui.theme.PostTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PostTheme {
                val currentUser = rememberAuthState()

                if (currentUser == null) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AuthScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
                else {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun rememberAuthState(): FirebaseUser? {
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    DisposableEffect(FirebaseAuth.getInstance()) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener(listener)

        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    return currentUser
}