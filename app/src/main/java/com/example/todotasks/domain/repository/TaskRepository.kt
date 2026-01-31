package com.example.todotasks.domain.repository

import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.data.database.entities.TaskEntity
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task

interface TaskRepository {

    //TaskDao
    suspend fun getTasks(): List<Task>
    suspend fun insertTask(task: Task)
    suspend fun deleteTask(id: Long)
    suspend fun updateTask(task: Task)

    //SubTaskDao
    suspend fun getSubTasks(): List<SubTask>
    suspend fun insertSubTask(subTask: SubTask)
    suspend fun deleteSubTask(id: Long)
    suspend fun updateTitleSubTask(id: Long, title: String)
    suspend fun updateCompletedSubTask(id: Long, completed: Boolean)


}