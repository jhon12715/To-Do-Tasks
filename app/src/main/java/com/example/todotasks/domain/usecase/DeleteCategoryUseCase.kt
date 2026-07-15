package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.core.ResultEvent
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class DeleteCategoryUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(category: Category): ResultEvent {
        repository.deleteCategory(category)
        return ResultEvent.Success("La categoría se ha borrado correctamente")
    }
}