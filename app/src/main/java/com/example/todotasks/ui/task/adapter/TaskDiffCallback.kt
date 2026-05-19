package com.example.todotasks.ui.task.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.task.model.TaskUI

class TaskDiffCallback: DiffUtil.ItemCallback<TaskUI>(){
    override fun areItemsTheSame(oldItem: TaskUI, newItem: TaskUI): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TaskUI, newItem: TaskUI): Boolean = oldItem.completedSubtask == newItem.completedSubtask && oldItem.totalSubTask == newItem.totalSubTask && oldItem.isCompleted == newItem.isCompleted && oldItem.task == newItem.task && oldItem.priority == newItem.priority && newItem.date == oldItem.date

}