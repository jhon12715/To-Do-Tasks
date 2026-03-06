package com.example.todotasks.domain.model

data class SubTask(
    val id: Long = 0,
    val idTask: Long,
    val title: String,
    val completed: Boolean = false)