package com.example.todotasks.domain.repository

import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.data.database.entities.TaskEntity
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.task.model.TaskUI
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    //TaskDao
    fun getTasks(): Flow<List<TaskUI>>
    suspend fun insertTask(task: Task): Long
    suspend fun deleteTask(id: Long)
    suspend fun updateTask(task: Task)
    suspend fun updateCompletedTask(id: Long, completed: Boolean)

    //SubTaskDao
    fun getSubTasks(idTask: Long): Flow<List<SubTask>>
    suspend fun insertSubTask(subTask: SubTask)
    suspend fun deleteSubTask(id: Long)
    suspend fun updateTitleSubTask(id: Long, title: String)
    suspend fun updateCompletedSubTask(id: Long, completed: Boolean)


}