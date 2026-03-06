package com.example.todotasks.domain.repository

import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.data.database.entities.TaskEntity
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    //TaskDao
    fun getTasks(): Flow<List<Task>>
    suspend fun insertTask(task: Task): Long
    suspend fun deleteTask(id: Long)
    suspend fun updateTask(task: Task)

    //SubTaskDao
    suspend fun getSubTasks(idTask: Long): List<SubTask>
    suspend fun insertSubTask(subTask: SubTask)
    suspend fun deleteSubTask(id: Long)
    suspend fun updateTitleSubTask(id: Long, title: String)
    suspend fun updateCompletedSubTask(id: Long, completed: Boolean)


}