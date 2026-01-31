package com.example.todotasks.data.repository

import com.example.todotasks.data.database.dao.SubTaskDao
import com.example.todotasks.data.database.dao.TaskDao
import com.example.todotasks.data.mapper.toDomain
import com.example.todotasks.data.mapper.toEntity
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDao: TaskDao, private val subTaskDao: SubTaskDao
) : TaskRepository {

    //TaskDao
    override suspend fun getTasks(): List<Task> = taskDao.getTasks().map { it.toDomain() }
    override suspend fun insertTask(task: Task) {
        val entity = task.toEntity()
        taskDao.insertTask(entity)
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteTask(id)
    }

    override suspend fun updateTask(task: Task) {
        val entity = task.toEntity()
        taskDao.updateTask(entity)
    }

    //SubTaskDao

    override suspend fun getSubTasks(): List<SubTask> =
        subTaskDao.getSubTasks().map { it.toDomain() }

    override suspend fun insertSubTask(subTask: SubTask) {
        val entity = subTask.toEntity()
        subTaskDao.insertSubTask(entity)
    }


    override suspend fun deleteSubTask(id: Long) {
        subTaskDao.deleteSubTask(id)
    }

    override suspend fun updateTitleSubTask(id: Long, title: String) {
        subTaskDao.updateTitleSubTask(id, title)
    }

    override suspend fun updateCompletedSubTask(id: Long, completed: Boolean) {
        subTaskDao.updateCompletedSubTask(id, completed)
    }

}