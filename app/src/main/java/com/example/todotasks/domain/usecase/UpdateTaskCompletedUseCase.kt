package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskCompletedUseCase @Inject constructor(
    val repository: TaskRepository
){

    suspend operator fun invoke(id: Long, isCompleted: Boolean){
        repository.updateCompletedTask(id, isCompleted)
    }
}