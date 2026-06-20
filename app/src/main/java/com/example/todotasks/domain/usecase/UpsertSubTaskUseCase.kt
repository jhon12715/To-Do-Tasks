package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.core.ResultEvent
import javax.inject.Inject

class UpsertSubTaskUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(subTask: SubTask): ResultEvent {

        if (subTask.title.trim().isEmpty()) {
            return ResultEvent.Error("El nombre no puede estar vacío")
        }

        return try {
            repository.upsertSubTask(subTask)
            if (subTask.id != 0L) {
                ResultEvent.Success("Se ha añadido la subtarea correctamente")
            } else {
                ResultEvent.Success("Se ha editado la subtarea correctamente")
            }
        } catch (e: SQLiteConstraintException) {
            ResultEvent.Error("La subtarea ya existe")
        }
    }
}