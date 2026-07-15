package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.core.ResultEvent
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class CategoryExistsUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(name: String, id: Long): Boolean {

        val nameTrimmed = name.trim()

        if (nameTrimmed.isEmpty() || nameTrimmed.equals("todas", ignoreCase = true)) return true

        return repository.categoryExists(nameTrimmed, id)
    }
}