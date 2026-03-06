package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateSubTaskNameUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long, title: String) {
        repository.updateTitleSubTask(id, title)
    }
}