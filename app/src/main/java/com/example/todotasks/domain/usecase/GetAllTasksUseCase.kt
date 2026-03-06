package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(private val repository: TaskRepository) {

    operator fun invoke(): Flow<List<Task>> {

        val tasks = repository.getTasks()

        return tasks.map { taskList ->
            taskList.map { task ->
                val subTasks = repository.getSubTasks(task.id)
                val total: Int = subTasks.size
                val completed: Int = subTasks.filter { subTask ->
                    subTask.completed
                }.size
                task.copy(subTaskCompleted = "$completed/$total")
            }
        }
    }
}