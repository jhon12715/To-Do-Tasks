package com.example.todotasks.ui.task.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.model.TaskUI

class TaskDiffCallback: DiffUtil.ItemCallback<TaskUI>(){
    override fun areItemsTheSame(oldItem: TaskUI, newItem: TaskUI): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TaskUI, newItem: TaskUI): Boolean = oldItem == newItem

}