package com.example.todotasks.domain.repository

import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.ParentTaskInfo
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskListItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {

    //TaskDao
    fun getAllTasksByCategory(categoryId: Long): Flow<List<TaskListItem>>
    fun getAllTasks(): Flow<List<TaskListItem>>
    suspend fun getTask(id: Long): Task
    suspend fun getParentTaskInfo(id: Long): ParentTaskInfo
    suspend fun upsertTask(task: Task): Long
    suspend fun deleteTasks(listId: List<Long>)
    suspend fun updateCompletedTask(id: Long, completed: Boolean)
    suspend fun taskExists(name: String, id: Long): Boolean
    suspend fun getTaskOldDate(taskId: Long): LocalDate?

    //SubTaskDao
    fun getAllSubTasks(idTask: Long): Flow<List<SubTask>>
    suspend fun upsertSubTask(subTask: SubTask)
    suspend fun deleteSubTask(listSubTaskId: List<Long>)
    suspend fun updateCompletedSubTask(id: Long, completed: Boolean)
    suspend fun subTaskExists(name: String, subTaskId: Long, taskId: Long): Boolean
    suspend fun getSubtaskById(subTaskId: Long): SubTask
    //CategoriesDao
    fun getAllCategories(): Flow<List<Category>>
    suspend fun deleteCategory(category: Category)
    suspend fun upsertCategory(category: Category)
    suspend fun categoryExists(name: String, id: Long): Boolean

}