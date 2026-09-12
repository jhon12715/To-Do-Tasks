package com.example.todotasks.ui.task.task_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeTaskListItem
import java.time.LocalDate

class TaskDiffCallback : DiffUtil.ItemCallback<TypeTaskListItem>() {
    override fun areItemsTheSame(oldItem: TypeTaskListItem, newItem: TypeTaskListItem): Boolean {
        return when {
            oldItem is TypeTaskListItem.HeaderItem && newItem is TypeTaskListItem.HeaderItem ->
                oldItem.header == newItem.header

            oldItem is TypeTaskListItem.TaskItem && newItem is TypeTaskListItem.TaskItem ->
                oldItem.task.id == newItem.task.id

            else -> false
        }
    }


    override fun areContentsTheSame(
        oldItem: TypeTaskListItem,
        newItem: TypeTaskListItem
    ): Boolean {
        return when {
            oldItem is TypeTaskListItem.HeaderItem && newItem is TypeTaskListItem.HeaderItem ->
                oldItem.header == newItem.header

            oldItem is TypeTaskListItem.TaskItem && newItem is TypeTaskListItem.TaskItem ->
                oldItem.task == newItem.task

            else -> false
        }
    }

    override fun getChangePayload(oldItem: TypeTaskListItem, newItem: TypeTaskListItem): Any? {
        return if (oldItem is TypeTaskListItem.TaskItem && newItem is TypeTaskListItem.TaskItem) {
            when {
                oldItem.task.tittle != newItem.task.tittle ->
                    TaskPayload.TittleChange(newItem.task.tittle)

                oldItem.task.isCompleted != newItem.task.isCompleted ->
                    TaskPayload.IsCompletedChange(newItem.task.isCompleted)

                oldItem.task.isSelectedToDelete != newItem.task.isSelectedToDelete ->
                    TaskPayload.IsSelectedToDeleteChange(newItem.task.isSelectedToDelete)

                oldItem.task.date != newItem.task.date ->
                    TaskPayload.DateChange(newItem.task.date)

                oldItem.task.subTaskCompleted != newItem.task.subTaskCompleted ->
                    TaskPayload.SubTaskCompletedChange(newItem.task.subTaskCompleted)

                else -> null
            }
        } else {
            null
        }
    }
}

sealed class TaskPayload() {
    data class TittleChange(val newTittle: String) : TaskPayload()
    data class IsCompletedChange(val newIsCompleted: Boolean) : TaskPayload()
    data class IsSelectedToDeleteChange(val newIsSelectedToDeleteChange: Boolean) : TaskPayload()
    data class DateChange(val newDate: LocalDate?) : TaskPayload()
    data class SubTaskCompletedChange(val newSubTaskCompleted: String) : TaskPayload()
}