package com.example.todotasks.ui.subTask.subtask_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todotasks.ui.model.TypeSubTaskListItem

class SubTaskDiffCallback : DiffUtil.ItemCallback<TypeSubTaskListItem>() {

    override fun areItemsTheSame(
        oldItem: TypeSubTaskListItem,
        newItem: TypeSubTaskListItem
    ): Boolean {
        return when {
            oldItem is TypeSubTaskListItem.SubTaskItem && newItem is TypeSubTaskListItem.SubTaskItem ->
                oldItem.subTaskUI.id == newItem.subTaskUI.id

            oldItem is TypeSubTaskListItem.HeaderItem && newItem is TypeSubTaskListItem.HeaderItem ->
                oldItem.header == newItem.header

            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: TypeSubTaskListItem,
        newItem: TypeSubTaskListItem
    ): Boolean {
        return when {
            oldItem is TypeSubTaskListItem.SubTaskItem && newItem is TypeSubTaskListItem.SubTaskItem ->
                oldItem.subTaskUI == newItem.subTaskUI

            oldItem is TypeSubTaskListItem.HeaderItem && newItem is TypeSubTaskListItem.HeaderItem ->
                oldItem.header == newItem.header

            else -> false
        }
    }

    override fun getChangePayload(
        oldItem: TypeSubTaskListItem,
        newItem: TypeSubTaskListItem
    ): Any? {
        return if (oldItem is TypeSubTaskListItem.SubTaskItem && newItem is TypeSubTaskListItem.SubTaskItem) {

            when {
                oldItem.subTaskUI.tittle != newItem.subTaskUI.tittle ->
                    PayloadSubtask.TittleSubTaskChange(newItem.subTaskUI.tittle)

                oldItem.subTaskUI.isCompleted != newItem.subTaskUI.isCompleted ->
                    PayloadSubtask.IsCompletedSubTaskChange(newItem.subTaskUI.isCompleted)

                oldItem.subTaskUI.isSelectedToDelete != newItem.subTaskUI.isSelectedToDelete ->
                    PayloadSubtask.IsSelectedToDeleteSubTaskChange(newItem.subTaskUI.isSelectedToDelete)

                else -> null
            }

        } else {
            null
        }
    }

}