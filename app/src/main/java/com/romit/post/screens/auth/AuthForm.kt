package com.romit.post.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romit.post.data.model.AuthModeConfig

@Composable
fun AuthForm(
    modifier: Modifier,
    config: AuthModeConfig,
    email: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    errorMessage: String?
) {
    // 4. State for the text fields should live inside the form itself.
    val passwordState = rememberTextFieldState()
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = modifier.padding(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                stringResource(config.screenTitle),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            SecureTextField(
                state = passwordState,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                textObfuscationMode = if (passwordHidden) TextObfuscationMode.RevealLastTyped else TextObfuscationMode.Visible,
                trailingIcon = {
                    IconButton(onClick = { passwordHidden = !passwordHidden }) {
                        val icon =
                            if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordHidden) "Show password" else "Hide password"
                        Icon(imageVector = icon, contentDescription = description)
                    }
                }
            )
            if (errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // 5. Update the password in the ViewModel just before triggering the action.
                    onPasswordChange(passwordState.text.toString())
                    config.onMainButtonClick()
                },
                enabled = email.isNotBlank() && passwordState.text.isNotBlank(),
                colors = ButtonDefaults.buttonColors(Color(0xff663399))
            ) {
                Text(stringResource(config.mainButtonText))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(stringResource(config.bottomText), style = MaterialTheme.typography.bodyMedium)
                Text(
                    stringResource(config.bottomClickableText),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable { config.onSwitchModeClick() },
                    color = Color(0xff663399),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}