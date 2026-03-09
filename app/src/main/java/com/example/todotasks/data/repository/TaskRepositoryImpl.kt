package com.example.todotasks.data.repository

import com.example.todotasks.data.database.dao.SubTaskDao
import com.example.todotasks.data.database.dao.TaskDao
import com.example.todotasks.data.mapper.toDomain
import com.example.todotasks.data.mapper.toEntity
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.task.model.TaskUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao, private val subTaskDao: SubTaskDao
) : TaskRepository {

    //TaskDao
    override fun getTasks(): Flow<List<TaskUI>> =
        taskDao.getTasks()

    override suspend fun insertTask(task: Task): Long {
        val entity = task.toEntity()
        return taskDao.insertTask(entity)
    }

    override suspend fun deleteTask(id: Long) {
        taskDao.deleteTask(id)
    }

    override suspend fun updateTask(task: Task) {
        val entity = task.toEntity()
        taskDao.updateTask(entity)
    }

    override suspend fun updateCompletedTask(id: Long, completed: Boolean) {
        taskDao.updateCompletedTask(id, completed)
    }

    //SubTaskDao

    override fun getSubTasks(idTask: Long): Flow<List<SubTask>> =
        subTaskDao.getSubTasks(idTask)

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