package com.example.todotasks.ui.task.task_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeTaskListItem

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
}