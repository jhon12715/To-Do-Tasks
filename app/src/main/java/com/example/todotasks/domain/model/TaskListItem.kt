package com.example.todotasks.domain.model

data class TaskListItem(
    val task: Task,
    val completedSubtask: Int,
    val totalSubTask: Int
)