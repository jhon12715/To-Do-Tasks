package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.core.ResultEvent
import javax.inject.Inject

class InsertCategoryUseCase @Inject constructor(private val repository: TaskRepository){

    suspend operator fun invoke(nameCategory: String): ResultEvent {

        val categoryTrim = nameCategory.trim()

        if (categoryTrim.isEmpty()) return ResultEvent.Error("El campo no puede ir vacío")

        return try{
            val category = Category(id = 0, name = categoryTrim)
            repository.insertCategory(category)
            ResultEvent.Success("Se ha añadido la categoría correctamente")
        }catch(e: SQLiteConstraintException){
            ResultEvent.Error("La categoría ya existe")
        }
    }
}