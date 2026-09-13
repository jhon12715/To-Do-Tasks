package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.notification.TaskNotificationAlarmManager
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.core.ResultEvent
import javax.inject.Inject

class UpsertTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val scheduler: TaskNotificationAlarmManager
) {

    suspend operator fun invoke(task: Task): ResultEvent {

        if (isNotValidLenght(task.tittle)) return ResultEvent.Error("Te has pasado del límite de carácteres")

        return if (task.tittle.trim().isNotEmpty()) {
            try {

                val isEditing = task.id != 0L
                val newTaskId = repository.upsertTask(task)

                val newRepository = task.copy(id = newTaskId)
                scheduler(newRepository)

                val successMessage = getSuccessMesage(isEditing)

                ResultEvent.Success(successMessage)
            } catch (e: SQLiteConstraintException) {

                ResultEvent.Error("La tarea ya existe")
            }
        } else {

            ResultEvent.Error("El nombre no puede ir vacío")
        }
    }

    private fun scheduler(task: Task) {
        scheduler.cancel(task)
        if (task.date == null) return

        scheduler.schedule(task)
    }

    private fun getSuccessMesage(isEditing: Boolean): String {
        return if (isEditing) {

            "Se ha editado la tarea correctamente"

        } else {

            "Se ha creado la tarea correctamente"

        }
    }

    private fun isNotValidLenght(name: String): Boolean = name.length > Task.TASK_TITTLE_MAX_LENGTH

}