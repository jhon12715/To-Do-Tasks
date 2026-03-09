package com.example.todotasks.ui.subTask.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.ui.task.model.TaskUI

class SubTaskDiffCallback: DiffUtil.ItemCallback<SubTask>(){
    override fun areItemsTheSame(oldItem: SubTask, newItem: SubTask): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SubTask, newItem: SubTask): Boolean = oldItem.title == newItem.title && oldItem.completed == newItem.completed

}