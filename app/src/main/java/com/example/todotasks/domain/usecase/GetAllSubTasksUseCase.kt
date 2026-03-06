package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.repository.TaskRepository
import javax.inject.Inject

class GetAllSubTasksUseCase @Inject constructor(private val repository: TaskRepository){

    suspend operator fun invoke(idTask: Long): List<SubTask> = repository.getSubTasks(idTask)

}