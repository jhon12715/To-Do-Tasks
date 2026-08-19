package com.example.todotasks.ui.subTask.subtask_list

data class SubtaskItemCallbacks(
    val updateSubTaskCompleted: (Long, Boolean) -> Unit,
    val editSubTaskForm: (Long) -> Unit,
    val addSubTaskToDeleteList: (Long) -> Unit,
    val removeSubTaskToDeleteList: (Long) -> Unit
)