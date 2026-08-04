package com.example.todotasks.ui.subTask.subtask_form

import com.example.todotasks.domain.model.TaskPriority

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

data class SubTaskFormState(
    val taskId: Long = 0,
    val subTaskId: Long = 0,
    val nameSubTask: String = "",
    val prioritySubTask: TaskPriority = TaskPriority.NORMAL,
    val isValid: Boolean = false
)