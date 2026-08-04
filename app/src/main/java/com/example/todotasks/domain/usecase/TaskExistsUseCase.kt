package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class TaskExistsUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(name: String, id: Long): Boolean {

        val nameTrimmed = name.trim()

        if (nameTrimmed.isEmpty()) return true

        return repository.taskExists(nameTrimmed, id)
    }
}