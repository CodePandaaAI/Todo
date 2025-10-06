package com.romit.post.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.romit.post.ui.theme.PostTheme
import com.romit.post.viewmodels.AuthViewModel

@Composable
fun AuthScreen(modifier: Modifier, authViewModel: AuthViewModel = viewModel()) {

    val uiState by authViewModel.uiState.collectAsState()


    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
            return@Column
        }
        Surface(
            modifier = Modifier.padding(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    "Create or Login Account",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = uiState.userEmail,
                    onValueChange = { authViewModel.changeEmail(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.userPassword,
                    onValueChange = { authViewModel.changePassword(it) },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.errorMessage != null) {
                    Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { authViewModel.signUp() },
                        enabled = uiState.userEmail.isNotBlank() && uiState.userPassword.isNotBlank()
                    ) {
                        Text("Sign Up")
                    }
                    Button(
                        onClick = { authViewModel.signIn() },
                        enabled = uiState.userEmail.isNotBlank() && uiState.userPassword.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            Color(0xff663399)
                        )
                    ) {
                        Text("Sign In")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    PostTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AuthScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}