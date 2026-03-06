package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.repository.TaskRepository
import javax.inject.Inject

class InsertSubTaskUseCase @Inject constructor(private val repository: TaskRepository){

    suspend operator fun invoke(subTask: SubTask){
        repository.insertSubTask(subTask)
    }
}