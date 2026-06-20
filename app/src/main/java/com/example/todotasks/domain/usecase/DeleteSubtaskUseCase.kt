package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.core.ResultEvent
import javax.inject.Inject

class DeleteSubtaskUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(id: Long): ResultEvent {
        repository.deleteSubTask(id)
        return ResultEvent.Success("La subtarea ha sido borrada correctamente")
    }
}