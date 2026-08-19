package com.example.todotasks.domain.model

data class SubTask(
    val id: Long = 0,
    val idTask: Long,
    val tittle: String,
    val completed: Boolean = false,
    val priority: TaskPriority = TaskPriority.NORMAL){
    companion object{
        const val SUBTASK_TITTLE_MAX_LENGTH = 120
    }
}