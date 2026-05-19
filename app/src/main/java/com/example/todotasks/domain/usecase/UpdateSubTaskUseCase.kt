package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.task.UiState
import javax.inject.Inject

class UpdateSubTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long, newTitle: String, oldTitle: String, newPriority: TaskPriority, oldPriority: TaskPriority): UiState {

        if(newTitle.trim().isEmpty()){
            return UiState.Error("El nombre no puede ir vacío")
        }

        val repeatTitle = oldTitle == newTitle
            val repeatPriority = oldPriority == newPriority

        if(repeatTitle && repeatPriority){
            return UiState.Error("No se ha editado nada")
        }

        return try {
            repository.updateSubTask(id, newTitle, newPriority)
            UiState.Success("La subtarea se ha añadido correctamente")
        } catch (e: SQLiteConstraintException){
            UiState.Error("La subtarea ya existe")
        }
    }
}