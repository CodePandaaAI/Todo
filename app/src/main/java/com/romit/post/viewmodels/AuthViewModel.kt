package com.romit.post.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romit.post.data.repositories.AuthRepositoryImpl
import com.romit.post.data.uistates.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()
    private val authRepository: AuthRepositoryImpl = AuthRepositoryImpl()

    fun signIn() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                authRepository.signIn(uiState.value.userEmail, uiState.value.userPassword)
                _uiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
    fun signUp() {

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                authRepository.signUp(uiState.value.userEmail, uiState.value.userPassword)
                _uiState.update { it.copy(isLoading = false, errorMessage = null) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun changeEmail(userEmail: String) {
        _uiState.update { it.copy(userEmail = userEmail) }
    }

    fun changePassword(userPassword: String) {
        _uiState.update { it.copy(userPassword = userPassword) }
    }
}