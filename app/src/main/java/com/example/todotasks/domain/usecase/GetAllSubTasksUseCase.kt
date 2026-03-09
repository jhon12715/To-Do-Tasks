package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSubTasksUseCase @Inject constructor(private val repository: TaskRepository){

    operator fun invoke(idTask: Long): Flow<List<SubTask>> = repository.getSubTasks(idTask)

}