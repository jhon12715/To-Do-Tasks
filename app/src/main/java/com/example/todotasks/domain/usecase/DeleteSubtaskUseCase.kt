package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.core.ResultEvent
import javax.inject.Inject

class DeleteSubtaskUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(listSubTaskId: List<Long>): ResultEvent {
        if (listSubTaskId.isEmpty()) return ResultEvent.Error("Debe seleccionar al menos una subtarea")
        repository.deleteSubTask(listSubTaskId)
        return if (listSubTaskId.size == 1){
            ResultEvent.Success("La subtarea ha sido borrada correctamente")
        } else{
            ResultEvent.Success("Las subtareas han sido borradas correctamente")
        }
    }
}