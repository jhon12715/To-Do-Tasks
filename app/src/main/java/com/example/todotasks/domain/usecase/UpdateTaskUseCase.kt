package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.notification.TaskNotificationAlarmManager
import com.example.todotasks.domain.notification.TaskNotificationWorkManager
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.task.UiState
import java.time.LocalDate
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(private val repository: TaskRepository, private val scheduler: TaskNotificationAlarmManager) {

    suspend operator fun invoke(
        id: Long,
        newTaskName: String,
        oldTaskName: String,
        newPriority: TaskPriority,
        oldPriority: TaskPriority,
        newDate: LocalDate?,
        oldDate: LocalDate?,
    ): UiState {
        if (newTaskName.isEmpty()) {
            return UiState.Error("El nombre no puede estar vacío")
        }
        val repeatName = newTaskName == oldTaskName
        val repeatPriority = newPriority == oldPriority
        val repeatDate = newDate == oldDate
        println("new: $newDate old: $oldDate")

        if (repeatName && repeatPriority && repeatDate) {
            return UiState.Error("No se ha editado nada")
        }
            try {
                val task = Task(id, newTaskName, newPriority, date = newDate)
                repository.updateTask(task)
                scheduler(task)

            } catch (e: SQLiteConstraintException) {
                return UiState.Error("La tarea ya existe")
            }


        return UiState.Success("La tarea se ha actualizado correctamente")

    }

    private fun scheduler(task: Task){
        //scheduler.cancel(task)

        if (task.date == null) return

        scheduler.schedule(task)
    }

}