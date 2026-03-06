package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateSubTaskCompletedUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long, completed: Boolean) {
        repository.updateCompletedSubTask(id, completed)
    }
}