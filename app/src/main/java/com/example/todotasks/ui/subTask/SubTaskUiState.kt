package com.example.todotasks.ui.subTask

sealed class SubTaskUiState() {
    data object Success : SubTaskUiState()
    data class UpdateError(val message: String) : SubTaskUiState()
    data class InsertError(val message: String) : SubTaskUiState()
    data object Loading : SubTaskUiState()
}