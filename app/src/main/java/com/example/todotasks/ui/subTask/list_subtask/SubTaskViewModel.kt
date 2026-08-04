package com.example.todotasks.ui.subTask.list_subtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.ParentTaskInfo
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskIsCompletedFilter
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.domain.usecase.DeleteSubtaskUseCase
import com.example.todotasks.domain.usecase.UpsertSubTaskUseCase
import com.example.todotasks.domain.usecase.UpdateSubTaskCompletedUseCase
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.model.SubTaskUI
import com.example.todotasks.ui.subTask.SubTaskFormState
import com.example.todotasks.ui.subTask.SubTaskUiEvent
import com.example.todotasks.ui.subTask.SubTaskUiState
import com.example.todotasks.ui.subTask.applySubTaskFilterAndSort
import com.example.todotasks.ui.subTask.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubTaskViewModel @Inject constructor(
    repository: TaskRepository,
    private val upsertSubTaskUseCase: UpsertSubTaskUseCase,
    private val updateSubTaskCompletedUseCase: UpdateSubTaskCompletedUseCase,
    private val deleteSubtaskUseCase: DeleteSubtaskUseCase
) :
    ViewModel() {

    private var originalSubtask: SubTask? = null

    private val _subTaskIsCompletedFilter: MutableStateFlow<TaskIsCompletedFilter> =
        MutableStateFlow(TaskIsCompletedFilter.ALL)

    private val _parentTaskInfo: MutableStateFlow<ParentTaskInfo> =
        MutableStateFlow(ParentTaskInfo())

    private val _isFormVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    //SubTask
    private val allSubTasks: StateFlow<List<SubTask>> = _parentTaskInfo.filterNotNull()
        .flatMapLatest { repository.getAllSubTasks(it.taskId) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    //Event Result
    private val _subTaskEventResult: MutableSharedFlow<ResultEvent> =
        MutableSharedFlow(replay = 0)
    val subTaskEventResult: SharedFlow<ResultEvent> = _subTaskEventResult

    // Ui State

    val subTaskUiState: StateFlow<SubTaskUiState> =
        combine(_subTaskIsCompletedFilter, allSubTasks, _isFormVisible) { filter, subTaskList, isFormVisible ->
            SubTaskUiState(
                title = _parentTaskInfo.value.taskName,
                filterSelected = filter,
                listSubtasks = subTaskList.applySubTaskFilterAndSort(filter),
                subTasksCompleted = subTaskList.count { it.completed },
                subTaskTotal = subTaskList.size
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            SubTaskUiState()
        )

    //Form
    private val _subTaskFormState: MutableStateFlow<SubTaskFormState> = MutableStateFlow(
        SubTaskFormState()
    )

    val subTaskFormState: StateFlow<SubTaskFormState> = _subTaskFormState

    fun init(taskId: Long, taskName: String) {
        _parentTaskInfo.update {
            ParentTaskInfo(taskId, taskName)
        }
    }

    fun onEvent(event: SubTaskUiEvent) {
        when (event) {
            is SubTaskUiEvent.UpdateFilterSubTaskCompleted -> updateFilterSubTaskCompleted(event.filterCompleted)
            SubTaskUiEvent.CreateNewSubTaskForm -> createNewSubtaskForm()
            is SubTaskUiEvent.EditSubTaskForm -> editSubTaskForm(event.subTask)
            is SubTaskUiEvent.UpdateSubTaskCompleted -> updateSubTaskCompleted(
                event.subTaskId,
                event.isCompleted
            )

            is SubTaskUiEvent.DeleteSubTask -> deleteSubtask(event.subTaskId)
            //Form
            is SubTaskUiEvent.ChangeSubTaskNameForm -> changeSubTaskNameForm(event.newName)
            is SubTaskUiEvent.ChangeSubTaskPriorityForm -> changeSubTaskPriorityForm(event.newPriority)
            SubTaskUiEvent.UpsertSubTask -> upsertSubTask()
            SubTaskUiEvent.CloseForm -> closeForm()
        }
    }

    private fun updateFilterSubTaskCompleted(filterCompleted: TaskIsCompletedFilter) {
        _subTaskIsCompletedFilter.update { filterCompleted }
    }

    private fun createNewSubtaskForm() {
        _subTaskFormState.update {
            originalSubtask = null
            SubTaskFormState()
        }

        _isFormVisible.value = true

    }

    private fun editSubTaskForm(subTask: SubTask) {
        _subTaskFormState.update {
            originalSubtask = subTask
            SubTaskFormState(
                subTaskId = subTask.id,
                subTasknameForm = subTask.title,
                priorityForm = subTask.priority,
                isValid = true
            )
        }

        _isFormVisible.value = true

    }

    private fun updateSubTaskCompleted(subTaskId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            updateSubTaskCompletedUseCase(subTaskId, isChecked)
        }
    }

    private fun deleteSubtask(id: Long) {
        viewModelScope.launch {
            _subTaskEventResult.emit(deleteSubtaskUseCase(id))
        }
    }

    private fun changeSubTaskNameForm(newName: String) {

        val id = subTaskFormState.value.subTaskId
        val nameTrimed = newName.trim()

        val isValid: Boolean = allSubTasks.value.none {
            it.title == nameTrimed && it.id != id
        } && nameTrimed.trim().isNotEmpty()

        _subTaskFormState.update {
            it.copy(subTasknameForm = nameTrimed, isValid = isValid)
        }


    }

    private fun changeSubTaskPriorityForm(newPriority: TaskPriority) {
        _subTaskFormState.update {
            it.copy(priorityForm = newPriority)
        }
    }

    private fun upsertSubTask() {
        viewModelScope.launch {

            val name = subTaskFormState.value.subTasknameForm
            val priority = subTaskFormState.value.priorityForm
            val taskId = _parentTaskInfo.value.taskId

            val subTask = originalSubtask?.copy(
                title = name,
                priority = priority
            ) ?: SubTask(
                title = name,
                priority = priority,
                idTask = taskId
            )

            _subTaskEventResult.emit(upsertSubTaskUseCase(subTask))

        }

    }

    private fun closeForm() {
        _isFormVisible.value = false
    }

}