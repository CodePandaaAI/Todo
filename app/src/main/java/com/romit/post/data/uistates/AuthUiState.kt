package com.romit.post.data.uistates

data class AuthUiState(
    val userEmail: String = "",
    val userPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
