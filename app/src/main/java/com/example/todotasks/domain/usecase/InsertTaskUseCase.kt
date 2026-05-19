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

class InsertTaskUseCase @Inject constructor(private val repository: TaskRepository, private val scheduler: TaskNotificationAlarmManager) {

    suspend operator fun invoke(taskName: String, priority: TaskPriority, date: LocalDate?): UiState {

        val taskNameTrimed = taskName.trim()
        return if(taskNameTrimed.isNotEmpty()) {
            val task = Task(task = taskName, priority = priority, date = date)
            try {
                val newRepository = repository.insertTask(task)
                scheduler(newRepository)
                UiState.Success("Se ha añadido la tarea correctamente")
            }catch (e: SQLiteConstraintException){
                UiState.Error("La tarea ya existe")
            }
        }else{
            UiState.Error("El nombre no puede ir vacío")
        }
    }

    private fun scheduler(task: Task){
        scheduler.cancel(task)
        if (task.date == null) return

        scheduler.schedule(task)
    }

}