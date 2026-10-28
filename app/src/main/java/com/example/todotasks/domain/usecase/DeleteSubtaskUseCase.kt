package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.domain.model.result_event.ResultEventSubtask
import javax.inject.Inject

class DeleteSubtaskUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(listSubTaskId: List<Long>): ResultEventSubtask {
        if (listSubTaskId.isEmpty()) return ResultEventSubtask.IsEmptyError
        repository.deleteSubTask(listSubTaskId)
        return if (listSubTaskId.size == 1){
            ResultEventSubtask.DeleteOneSubtaskSuccess
        } else{
            ResultEventSubtask.DeleteMultipleSubtaskSuccess
        }
    }
}