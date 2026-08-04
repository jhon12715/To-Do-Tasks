package com.example.todotasks.ui.subTask.list_subtask.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.ui.model.TaskUI
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
        return when{
            oldItem is TypeSubTaskListItem.SubTaskItem && newItem is TypeSubTaskListItem.SubTaskItem ->
                oldItem.subTaskUI == newItem.subTaskUI

            oldItem is TypeSubTaskListItem.HeaderItem && newItem is TypeSubTaskListItem.HeaderItem ->
                oldItem.header == newItem.header

            else -> false
        }
    }

}