package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.notification.TaskNotificationAlarmManager
import com.example.todotasks.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository, private val scheduler: TaskNotificationAlarmManager){

    suspend operator fun invoke(id: Long){
        val task = repository.getTask(id)
        scheduler.cancel(task)
        repository.deleteTask(id)
    }
}