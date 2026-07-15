package com.example.todotasks.data.repository

import android.app.TaskInfo
import com.example.todotasks.data.database.dao.CategoryDao
import com.example.todotasks.data.database.dao.SubTaskDao
import com.example.todotasks.data.database.dao.TaskDao
import com.example.todotasks.data.mapper.toDomain
import com.example.todotasks.data.mapper.toEntity
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.ParentTaskInfo
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskListItem
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.model.TaskUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val subTaskDao: SubTaskDao,
    private val categoryDao: CategoryDao
) : TaskRepository {

    //TaskDao
    override fun getTasks(): Flow<List<TaskListItem>> = taskDao.getAllTasks()
        .map { list ->
            list.map { item -> item.toDomain() }
        }

    override suspend fun getTask(id: Long): Task = taskDao.getTask(id)

    override suspend fun getParentTaskInfo(id: Long): ParentTaskInfo = taskDao.getParentTaskInfo(id)

    override suspend fun upsertTask(task: Task): Long {
        return taskDao.upsertTask(task.toEntity())
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteTask(id)
    }

    override suspend fun updateCompletedTask(id: Long, completed: Boolean) {
        taskDao.updateCompletedTask(id, completed)
    }

    override suspend fun taskExists(name: String, id: Long): Boolean = taskDao.taskExists(name, id)

//SubTaskDao

    override fun getAllSubTasks(idTask: Long): Flow<List<SubTask>> =
        subTaskDao.getAllSubTasks(idTask)


    override suspend fun upsertSubTask(subTask: SubTask) {
        val entity = subTask.toEntity()
        subTaskDao.upsertSubTask(entity)
    }


    override suspend fun deleteSubTask(id: Long) {
        subTaskDao.deleteSubTask(id)
    }

    override suspend fun updateCompletedSubTask(id: Long, completed: Boolean) {
        subTaskDao.updateCompletedSubTask(id, completed)
    }

//CategoriesDao

    override fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }

    override suspend fun upsertCategory(category: Category) {
        categoryDao.upsertCategory(category.toEntity())
    }

    override suspend fun categoryExists(name: String, id: Long): Boolean = categoryDao.categoryExists(name, id)

}