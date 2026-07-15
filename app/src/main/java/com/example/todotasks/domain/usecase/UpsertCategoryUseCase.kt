package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.core.ResultEvent
import javax.inject.Inject

class UpsertCategoryUseCase @Inject constructor(private val repository: TaskRepository){

    suspend operator fun invoke(category: Category): ResultEvent {

        val categoryTrim = category.name.trim()

        if (categoryTrim.isEmpty()) return ResultEvent.Error("El campo no puede ir vacío")

        return try{
            println("category new: $category")
            repository.upsertCategory(category)
            ResultEvent.Success("Se ha añadido la categoría correctamente")
        }catch(e: SQLiteConstraintException){
            ResultEvent.Error("La categoría ya existe")
        }
    }
}