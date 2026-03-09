package com.example.todotasks.domain.model

data class Task(val id: Long = 0, val task: String, val isCompleted: Boolean = false)