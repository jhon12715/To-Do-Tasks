package com.example.todotasks.ui.core

sealed class ResultEvent(val message: String) {
    data class Success(val succesMessage: String) : ResultEvent(succesMessage)
    data class Error(val errorMessage: String) : ResultEvent(errorMessage)
}