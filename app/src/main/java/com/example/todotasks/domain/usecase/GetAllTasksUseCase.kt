package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.task.model.TaskUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(): Flow<List<TaskUI>> {

        val tasks:List<TaskUI> = repository.getTasks().first()
        println(tasks)
        return repository.getTasks()
    }
}