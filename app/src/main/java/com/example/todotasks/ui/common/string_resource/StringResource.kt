package com.example.todotasks.ui.common.string_resource

import androidx.annotation.StringRes
import com.example.todotasks.R
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.model.result_event.AppResultEvent
import com.example.todotasks.domain.model.result_event.ResultEventCategory
import com.example.todotasks.domain.model.result_event.ResultEventCommon
import com.example.todotasks.domain.model.result_event.ResultEventSubtask
import com.example.todotasks.domain.model.result_event.ResultEventTask
import com.example.todotasks.ui.model.DateHeaderSorter

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

object StringResource {

    @StringRes
    fun getDateHeaderResource(header: String): Int = when (DateHeaderSorter.valueOf(header)) {
        DateHeaderSorter.HOY -> R.string.today_date
        DateHeaderSorter.FUTURAS -> R.string.future_date
        DateHeaderSorter.VENCIDAS -> R.string.overdue_date
        DateHeaderSorter.SIN_FECHA -> R.string.without_date
    }

    @StringRes
    fun getPriorityStringResource(priority: TaskPriority): Int = when (priority) {
        TaskPriority.ALTA -> R.string.high_priority
        TaskPriority.NORMAL -> R.string.medium_priority
        TaskPriority.BAJA -> R.string.low_priority
    }

    @StringRes
    fun getResultEventStringResource(event: AppResultEvent): Int = when (event) {
        ResultEventCategory.CategoryAlreadyExistsError -> R.string.result_event_category_already_exists_error
        ResultEventCategory.CreateSuccess -> R.string.result_event_category_create_success
        ResultEventCategory.DeleteSuccess -> R.string.result_event_delete_category_success
        ResultEventCategory.UpdateSuccess -> R.string.result_event_category_update_success
        ResultEventSubtask.CreateSuccess -> R.string.result_event_subtask_create_success
        ResultEventSubtask.DeleteMultipleSubtaskSuccess -> R.string.result_event_delete_multiple_subtask_success
        ResultEventSubtask.DeleteOneSubtaskSuccess -> R.string.result_event_delete_one_subtask_success
        ResultEventSubtask.IsEmptyError -> R.string.result_event_is_empty_subtask_error
        ResultEventSubtask.SubtaskAlreadyExistsError -> R.string.result_event_subtask_already_exists_error
        ResultEventSubtask.UpdateSuccess -> R.string.result_event_subtask_update_success
        ResultEventTask.CreateSuccess -> R.string.result_event_task_create_success
        ResultEventTask.DeleteMultipleTaskSuccess -> R.string.result_event_delete_multiple_task_success
        ResultEventTask.DeleteOneTaskSuccess -> R.string.result_event_delete_one_task_success
        ResultEventTask.IsEmptyError -> R.string.result_event_is_empty_task_error
        ResultEventTask.TaskAlreadyExistsError -> R.string.result_event_task_already_exists_error
        ResultEventTask.UpdateSuccess -> R.string.result_event_task_update_success
        ResultEventCommon.EmptyTextError -> R.string.result_event_empty_text_error
        ResultEventCommon.LengthNotValidError -> R.string.result_event_length_not_valid_error
    }
}