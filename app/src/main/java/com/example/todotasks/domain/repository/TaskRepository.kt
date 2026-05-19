package com.example.todotasks.domain.repository

import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.task.model.TaskUI
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    //TaskDao
    fun getTasks(): Flow<List<TaskUI>>
    suspend fun insertTask(task: Task): Task
    suspend fun deleteTask(id: Long)
    suspend fun updateTask(task: Task)
    suspend fun updateCompletedTask(id: Long, completed: Boolean)

    //SubTaskDao
    fun getAllSubTasks(idTask: Long): Flow<List<SubTask>>
    suspend fun insertSubTask(subTask: SubTask)
    suspend fun deleteSubTask(id: Long)
    suspend fun updateSubTask(id: Long, title: String, priority: TaskPriority)
    suspend fun updateCompletedSubTask(id: Long, completed: Boolean)


}