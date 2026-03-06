package com.example.todotasks.ui.task

sealed class UiState() {
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
    data object Loading : UiState()
}