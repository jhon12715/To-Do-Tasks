package com.example.todotasks.ui.task.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todotasks.R
import com.example.todotasks.ui.task.TaskItemCallbacks
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.task.adapter.TaskViewHolder

class TaskListAdapter(
    private val callbacks: TaskItemCallbacks
) : ListAdapter<TaskUI, TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.render(getItem(position), callbacks)
    }

    override fun getItemId(position: Int): Long {
        return super.getItem(position).id
    }

    init {
        setHasStableIds(true)
    }

}