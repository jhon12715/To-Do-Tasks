package com.example.todotasks.domain.model

import java.time.LocalDate

data class Task(val id: Long = 0, val task: String, val priority: TaskPriority, val isCompleted: Boolean = false, val date: LocalDate? = null)