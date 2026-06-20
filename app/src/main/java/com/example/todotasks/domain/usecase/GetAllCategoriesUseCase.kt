package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val repository: TaskRepository){

    operator fun invoke(): Flow<List<Category>> = repository.getAllCategories()
}