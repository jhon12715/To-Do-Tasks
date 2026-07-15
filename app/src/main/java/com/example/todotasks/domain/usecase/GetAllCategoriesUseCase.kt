package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.model.CategoryUI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val repository: TaskRepository){

    operator fun invoke(): Flow<List<Category>> {
        return repository.getAllCategories()}
}