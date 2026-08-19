package com.example.todotasks.ui.core

sealed class ResultEvent(val message: String) {
    data class Success(val successMessage: String) : ResultEvent(successMessage)
    data class Error(val errorMessage: String) : ResultEvent(errorMessage)
}