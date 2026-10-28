package com.example.todotasks.ui.task.task_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.R
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.model.result_event.AppResultEvent
import com.example.todotasks.domain.usecase.GetAllCategoriesUseCase
import com.example.todotasks.domain.usecase.TaskExistsUseCase
import com.example.todotasks.domain.usecase.UpsertTaskUseCase
import com.example.todotasks.ui.common.string_resource.StringProvider
import com.example.todotasks.ui.mapper.toFormUi
import com.example.todotasks.ui.model.TaskUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val taskExistsUseCase: TaskExistsUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val stringProvider: StringProvider
) : ViewModel() {
    //Result
    private val _resultEvent: MutableSharedFlow<AppResultEvent> = MutableSharedFlow(replay = 0)
    val resultEvent: SharedFlow<AppResultEvent> = _resultEvent

    //Task Form
    private val _taskFormState: MutableStateFlow<TaskFormState> = MutableStateFlow(TaskFormState())
    val taskFormState: StateFlow<TaskFormState> = _taskFormState

    private var originalTask: TaskUI? = null

    fun onEvent(event: TaskFormEvent) {
        when (event) {
            is TaskFormEvent.UpdateNameTaskForm -> updateNameTaskForm(event.taskForm)
            is TaskFormEvent.UpdateDateTaskForm -> updateDateTaskForm(event.dateForm)
            is TaskFormEvent.UpdateCategoryIdTaskForm -> updateCategoryIdTaskSelectedForm(event.categoryFormSelectedId)
            is TaskFormEvent.UpdatePriorityTaskForm -> updatePriorityTaskForm(event.taskPriorityForm)
            TaskFormEvent.UpsertTaskForm -> upsertTaskForm()
            is TaskFormEvent.OpeningForm -> openingForm(event.task)
        }
    }

    private fun updateNameTaskForm(nameTaskForm: String) {

        viewModelScope.launch {
            _taskFormState.update {
                it.copy(
                    name = nameTaskForm,
                    isValid = !taskExistsUseCase(nameTaskForm, taskFormState.value.id)
                )
            }
        }


    }

    private fun updateDateTaskForm(newDateForm: LocalDate?) {
        _taskFormState.update { it.copy(date = newDateForm) }
    }

    private fun updateCategoryIdTaskSelectedForm(categoryFormSelectedId: Long?) {
        _taskFormState.update { it.copy(categoryId = categoryFormSelectedId) }
    }

    private fun updatePriorityTaskForm(taskPriorityForm: TaskPriority) {
        _taskFormState.update { it.copy(priority = taskPriorityForm) }
    }

    private fun upsertTaskForm() {

        viewModelScope.launch {
            val newTask: Task = taskFormToTask()
            _resultEvent.emit(upsertTaskUseCase(newTask))
        }

    }

    private fun openingForm(task: TaskUI) {
        viewModelScope.launch {
            val categories = getAllCategoriesUseCase().map {
                it.toFormUi(stringProvider.getStringResource(R.string.uncategorized))
            }.first()
            originalTask = task
            _taskFormState.update {
                println("item: $task")
                TaskFormState(
                    id = task.id,
                    name = task.tittle,
                    priority = task.priority,
                    date = task.date,
                    categoryId = task.categoryId,
                    isValid = task.tittle.isNotEmpty(),
                    isReady = true,
                    availableCategories = categories
                )
            }
        }
    }

    private fun taskFormToTask(): Task {
        val taskForm = taskFormState.value

        return Task(
            id = originalTask!!.id,
            tittle = taskForm.name,
            priority = taskForm.priority,
            isCompleted = originalTask!!.isCompleted,
            date = taskForm.date,
            categoryId = taskForm.categoryId
        )
    }

}