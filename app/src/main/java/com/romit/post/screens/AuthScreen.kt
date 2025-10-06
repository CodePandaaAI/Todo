package com.romit.post.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

    // Create TextFieldState for password
    val passwordState = rememberTextFieldState(initialText = uiState.userPassword)

    // Track password visibility
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    var isLoginScreen by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoginScreen) {

            LoginScreen(
                modifier = Modifier,
                isLoading = uiState.isLoading,
                userEmail = uiState.userEmail,
                passwordState = passwordState,
                passwordHidden = passwordHidden,
                switchHiddenPassword = { passwordHidden = !passwordHidden },
                errorMessage = uiState.errorMessage,
                onEmailChange = { authViewModel.changeEmail(it) },
                onSignUpClicked = { isLoginScreen = false },
                changePassword = { authViewModel.changePassword(it) },
                signIn = { authViewModel.signIn() }
            )


        } else {
            SignUpScreen(
                modifier = Modifier,
                isLoading = uiState.isLoading,
                userEmail = uiState.userEmail,
                passwordState = passwordState,
                passwordHidden = passwordHidden,
                switchHiddenPassword = { passwordHidden = !passwordHidden },
                errorMessage = uiState.errorMessage,
                onEmailChange = { authViewModel.changeEmail(it) },
                onLogInClicked = { isLoginScreen = true },
                changePassword = { authViewModel.changePassword(it) },
                signUp = { authViewModel.signUp() }
            )
        }
    }
}


@Composable
fun LoginScreen(
    modifier: Modifier,
    isLoading: Boolean,
    userEmail: String,
    passwordState: TextFieldState,
    passwordHidden: Boolean,
    switchHiddenPassword: () -> Unit,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onSignUpClicked: () -> Unit,
    changePassword: (String) -> Unit,
    signIn: () -> Unit
) {
    if (isLoading) {
        CircularProgressIndicator()
        return
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
                "Login Account",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = userEmail,
                onValueChange = { onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // New SecureTextField for password
            SecureTextField(
                state = passwordState,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                textObfuscationMode = if (passwordHidden)
                    TextObfuscationMode.RevealLastTyped
                else
                    TextObfuscationMode.Visible,
                trailingIcon = {
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    IconButton(onClick = { switchHiddenPassword() }) {
                        val visibilityIcon = if (passwordHidden)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                }
            )
            if (errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    changePassword(passwordState.text.toString())
                    signIn()
                },
                enabled = userEmail.isNotBlank() && passwordState.text.isNotBlank(),
                colors = ButtonDefaults.buttonColors(Color(0xff663399))
            ) {
                Text("Sign In")
            }
            Row(
                modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Sign Up Now",
                    modifier.clickable { onSignUpClicked() },
                    color = Color(0xff663399),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun SignUpScreen(
    modifier: Modifier,
    isLoading: Boolean,
    userEmail: String,
    passwordState: TextFieldState,
    passwordHidden: Boolean,
    switchHiddenPassword: () -> Unit,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onLogInClicked: () -> Unit,
    changePassword: (String) -> Unit,
    signUp: () -> Unit
) {
    if (isLoading) {
        CircularProgressIndicator()
        return
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
                "Create Account",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = userEmail,
                onValueChange = { onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // New SecureTextField for password
            SecureTextField(
                state = passwordState,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                textObfuscationMode = if (passwordHidden)
                    TextObfuscationMode.RevealLastTyped
                else
                    TextObfuscationMode.Visible,
                trailingIcon = {
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    IconButton(onClick = { switchHiddenPassword() }) {
                        val visibilityIcon = if (passwordHidden)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                }
            )
            if (errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    changePassword(passwordState.text.toString())
                    signUp()
                },
                enabled = userEmail.isNotBlank() && passwordState.text.isNotBlank(),
                colors = ButtonDefaults.buttonColors(Color(0xff663399))
            ) {
                Text("Register")
            }
            Row(
                modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an Account? ", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "LogIn Now",
                    modifier.clickable { onLogInClicked() },
                    color = Color(0xff663399),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    PostTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LoginScreen(
                modifier = Modifier.padding(innerPadding),
                isLoading = false,
                userEmail = "",
                passwordState = rememberTextFieldState(""),
                passwordHidden = true,
                switchHiddenPassword = { },
                errorMessage = null,
                onEmailChange = { },
                onSignUpClicked = {},
                changePassword = { },
                signIn = { }
            )
        }
    }
}