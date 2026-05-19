package com.example.todotasks.data.repository

import com.example.todotasks.data.database.dao.SubTaskDao
import com.example.todotasks.data.database.dao.TaskDao
import com.example.todotasks.data.mapper.toEntity
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.task.model.TaskUI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao, private val subTaskDao: SubTaskDao
) : TaskRepository {

    //TaskDao
    override fun getTasks(): Flow<List<TaskUI>> = taskDao.getAllTasks()

    override suspend fun insertTask(task: Task): Task {
        val id = taskDao.insertTask(task.toEntity())
        return task.copy(id = id)
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteTask(id)
    }

    override suspend fun updateTask(task: Task) {

        taskDao.updateTask(task.id, task.task, task.priority, task.date)
    }

    override suspend fun updateCompletedTask(id: Long, completed: Boolean) {
        taskDao.updateCompletedTask(id, completed)
    }

//SubTaskDao

    override fun getAllSubTasks(idTask: Long): Flow<List<SubTask>> =
        subTaskDao.getAllSubTasks(idTask)


    override suspend fun insertSubTask(subTask: SubTask) {
        val entity = subTask.toEntity()
        subTaskDao.insertSubTask(entity)
    }


    override suspend fun deleteSubTask(id: Long) {
        subTaskDao.deleteSubTask(id)
    }

    override suspend fun updateSubTask(id: Long, title: String, priority: TaskPriority) {
        subTaskDao.updateSubTask(id, title, priority)
    }

    override suspend fun updateCompletedSubTask(id: Long, completed: Boolean) {
        subTaskDao.updateCompletedSubTask(id, completed)
    }

}