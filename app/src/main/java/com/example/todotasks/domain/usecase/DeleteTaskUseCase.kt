package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.notification.TaskNotificationAlarmManager
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.domain.model.result_event.ResultEventTask
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val scheduler: TaskNotificationAlarmManager
) {

    suspend operator fun invoke(taskList: List<Task>): ResultEventTask {
        if (taskList.isEmpty()) return ResultEventTask.IsEmptyError
        val idList = taskList.map { task -> task.id }

        repository.deleteTasks(idList)

        taskList.forEach {
            task -> scheduler.cancel(task)

        }
        return if (idList.size == 1) {
            ResultEventTask.DeleteOneTaskSuccess
        } else {
            ResultEventTask.DeleteMultipleTaskSuccess
        }
    }
}