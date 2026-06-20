package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.TaskPriority

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

data class SubTaskFormState(
    val subTaskId: Long = 0L,
    val subTasknameForm: String = "",
    val priorityForm: TaskPriority = TaskPriority.NORMAL,
    val isValid: Boolean = false
)