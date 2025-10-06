package com.romit.post.screens.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.romit.post.R
import com.romit.post.data.model.AuthModeConfig
import com.romit.post.ui.theme.PostTheme
import com.romit.post.viewmodels.auth.AuthViewModel

@Composable
fun AuthScreen(modifier: Modifier = Modifier, authViewModel: AuthViewModel = viewModel()) {

    val uiState by authViewModel.uiState.collectAsState()
    var isLoginScreen by rememberSaveable { mutableStateOf(true) }

    // 2. Define the configurations for both login and sign-up modes.
    val loginConfig = AuthModeConfig(
        screenTitle = R.string.login_account,
        mainButtonText = R.string.sign_in,
        bottomText = R.string.don_t_have_an_account,
        bottomClickableText = R.string.sign_up_now,
        onMainButtonClick = { authViewModel.signIn() },
        onSwitchModeClick = { isLoginScreen = false }
    )

    val signUpConfig = AuthModeConfig(
        screenTitle = R.string.create_account,
        mainButtonText = R.string.register,
        bottomText = R.string.already_have_an_account,
        bottomClickableText = R.string.login_now,
        onMainButtonClick = { authViewModel.signUp() },
        onSwitchModeClick = { isLoginScreen = true }
    )

    // Select the current configuration based on the state.
    val currentConfig = if (isLoginScreen) loginConfig else signUpConfig

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show loading indicator on top of everything if loading.
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            AuthForm(
                modifier = Modifier,
                config = currentConfig,
                email = uiState.userEmail,
                onEmailChange = { authViewModel.changeEmail(it) },
                onPasswordChange = { authViewModel.changePassword(it) },
                errorMessage = uiState.errorMessage
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    PostTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AuthForm(
                modifier = Modifier.padding(innerPadding),
                config = AuthModeConfig(
                    screenTitle = R.string.login_account,
                    mainButtonText = R.string.sign_in,
                    bottomText = R.string.don_t_have_an_account,
                    bottomClickableText = R.string.sign_up_now,
                    onMainButtonClick = { },
                    onSwitchModeClick = { }
                ),
                email = "test@example.com",
                onEmailChange = { },
                onPasswordChange = { },
                errorMessage = "This is an error message."
            )
        }
    }
}