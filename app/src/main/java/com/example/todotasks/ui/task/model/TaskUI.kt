package com.example.todotasks.ui.task.model

data class TaskUI(val id: Long, val task: String, val completedSubtask: Int, val totalSubTask: Int, val isCompleted: Boolean){
    val subTaskCompleted: String
        get() = "$completedSubtask/$totalSubTask"
}