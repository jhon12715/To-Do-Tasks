package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.notification.TaskNotificationAlarmManager
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.ui.core.ResultEvent
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val scheduler: TaskNotificationAlarmManager
) {

    suspend operator fun invoke(listId: List<Long>): ResultEvent {
        //val task = repository.getTask(id)
        //println("id: $id task: $task")
        //scheduler.cancel(task)
        if (listId.isEmpty()) return ResultEvent.Error("Debe seleccionar al menos una subtarea")
        repository.deleteTasks(listId)
        return if (listId.size == 1) {
            ResultEvent.Success("La tarea se ha borrado correctamente")
        } else {
            ResultEvent.Success("Las tareas se han borrado correctamente")
        }
    }
}