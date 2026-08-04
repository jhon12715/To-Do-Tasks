package com.example.todotasks.domain.model

import java.time.LocalDate

data class Task(
    val id: Long = 0L,
    val tittle: String,
    val priority: TaskPriority = TaskPriority.NORMAL,
    val isCompleted: Boolean = false,
    val date: LocalDate? = null,
    val categoryId: Long? = null
)