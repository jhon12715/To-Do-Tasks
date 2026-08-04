package com.example.todotasks.ui.subTask.subtask_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.domain.usecase.SubTaskExistsUseCase
import com.example.todotasks.domain.usecase.UpsertSubTaskUseCase
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.model.SubTaskUI
import com.example.todotasks.ui.subTask.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@HiltViewModel
class SubTaskFormViewModel @Inject constructor(
    private val subTaskExistsUseCase: SubTaskExistsUseCase,
    private val upsertSubTaskUseCase: UpsertSubTaskUseCase
) : ViewModel() {

    private val _subTaskFormState: MutableStateFlow<SubTaskFormState> =
        MutableStateFlow(SubTaskFormState())
    val subTaskFormState: StateFlow<SubTaskFormState> = _subTaskFormState

    private val _resultEvent: MutableSharedFlow<ResultEvent> = MutableSharedFlow(replay = 0)
    val resultEvent: SharedFlow<ResultEvent> = _resultEvent

    private var originalSubTask: SubTask = SubTask()

    fun onEvent(event: SubTaskFormEvent) {
        when (event) {
            is SubTaskFormEvent.OpeningForm -> openingForm(event.subTask)
            is SubTaskFormEvent.UpdateSubTaskName -> updateSubTaskName(event.name)
            is SubTaskFormEvent.UpdateSubTaskPriority -> updateSubTaskPriority(event.priority)
            SubTaskFormEvent.CommitForm -> commitForm()
        }
    }

    private fun openingForm(subTask: SubTaskUI) {
        originalSubTask = subTask.toDomain()
        _subTaskFormState.update {
            SubTaskFormState(
                taskId = originalSubTask.idTask,
                subTaskId = originalSubTask.id,
                nameSubTask = originalSubTask.title,
                prioritySubTask = originalSubTask.priority,
                isValid = originalSubTask.id != 0L,
            )
        }
    }

    private fun updateSubTaskName(name: String) {
        val subTaskForm = _subTaskFormState.value
        viewModelScope.launch {
            _subTaskFormState.update {
                it.copy(
                    nameSubTask = name,
                    isValid = !subTaskExistsUseCase(
                        name,
                        subTaskForm.subTaskId,
                        subTaskForm.taskId
                    )
                )
            }
        }

    }

    private fun updateSubTaskPriority(priority: TaskPriority) {
        _subTaskFormState.update {
            it.copy(
                prioritySubTask = priority
            )
        }
    }

    private fun commitForm() {
        viewModelScope.launch {
            val subTask = getSubTaskToCommit()
            _resultEvent.emit(upsertSubTaskUseCase(subTask))
        }
    }

    private fun getSubTaskToCommit(): SubTask {
        val subTaskForm = _subTaskFormState.value

        return SubTask(
            id = originalSubTask.id,
            idTask = originalSubTask.idTask,
            title = subTaskForm.nameSubTask,
            completed = originalSubTask.completed,
            priority = subTaskForm.prioritySubTask
        )
    }
}