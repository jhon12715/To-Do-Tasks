package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.model.result_event.ResultEventSubtask
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.result_event.AppResultEvent
import com.example.todotasks.domain.model.result_event.ResultEventCommon
import com.example.todotasks.domain.repository.TaskRepository
import javax.inject.Inject

class UpsertSubTaskUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(subTask: SubTask): AppResultEvent {

        if (isNotValidLenght(subTask.tittle)) return ResultEventCommon.LengthNotValidError

        if (subTask.tittle.trim().isEmpty()) {
            return ResultEventCommon.EmptyTextError
        }

        return try {
            repository.upsertSubTask(subTask)

            val isEditing = subTask.idTask > 0L
            return getSuccessMessage(isEditing)
        } catch (e: SQLiteConstraintException) {
            ResultEventSubtask.SubtaskAlreadyExistsError
        }
    }

    private fun isNotValidLenght(name: String): Boolean =
        name.length > SubTask.SUBTASK_TITTLE_MAX_LENGTH

    private fun getSuccessMessage(isEditing: Boolean) =
        if (isEditing)
            ResultEventSubtask.UpdateSuccess
        else
            ResultEventSubtask.CreateSuccess
}