package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.notification.TaskNotificationAlarmManager
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.domain.model.result_event.ResultEventTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.result_event.AppResultEvent
import com.example.todotasks.domain.model.result_event.ResultEventCommon
import java.time.LocalDate
import javax.inject.Inject

class UpsertTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val scheduler: TaskNotificationAlarmManager
) {

    suspend operator fun invoke(task: Task): AppResultEvent {

        if (isNotValidLenght(task.tittle)) return ResultEventCommon.LengthNotValidError

        return if (task.tittle.trim().isNotEmpty()) {
            try {
                val isEditing = task.id > 0L
                val oldDate = getOldDate(isEditing, task.id)

                val newTask = upsertTask(isEditing, task)

                if (!dateIsTheSame(oldDate, newTask.date)) scheduler(newTask)

                getSuccessMessage(isEditing)
            } catch (e: SQLiteConstraintException) {
                ResultEventTask.TaskAlreadyExistsError
            }
        } else {
            ResultEventCommon.EmptyTextError
        }
    }

    private suspend fun getOldDate(isEditing: Boolean, taskId: Long): LocalDate? =
        if (isEditing) repository.getTaskOldDate(taskId) else null

    private suspend fun upsertTask(isEditing: Boolean, task: Task): Task {

        val newTaskId = repository.upsertTask(task).run {
            if (isEditing) {
                task.id
            } else {
                this
            }
        }
        return task.copy(id = newTaskId)
    }

    private fun dateIsTheSame(oldDate: LocalDate?, newDate: LocalDate?): Boolean {
        return oldDate == newDate
    }

    private fun scheduler(task: Task) {
        scheduler.cancel(task)
        if (task.date == null) return
        scheduler.schedule(task)
    }

    private fun getSuccessMessage(isEditing: Boolean): ResultEventTask = if (isEditing) {
        ResultEventTask.UpdateSuccess
    } else {
        ResultEventTask.CreateSuccess
    }


    private fun isNotValidLenght(name: String): Boolean = name.length > Task.TASK_TITTLE_MAX_LENGTH

}