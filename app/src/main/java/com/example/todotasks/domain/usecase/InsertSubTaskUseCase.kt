package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.task.UiState
import javax.inject.Inject

class InsertSubTaskUseCase @Inject constructor(private val repository: TaskRepository){

    suspend operator fun invoke(idTask: Long, title: String, priority: TaskPriority): UiState{

        if (title.trim().isEmpty()){
            return UiState.Error("El nombre no puede estar vacío")
        }

        val subtask = SubTask(idTask = idTask, title = title, priority = priority)

        return try {
            repository.insertSubTask(subtask)
            UiState.Success("Se ha añadido la subtarea correctamente")
        }catch (e: SQLiteConstraintException){
            UiState.Error("La subtarea ya existe")
        }
    }
}