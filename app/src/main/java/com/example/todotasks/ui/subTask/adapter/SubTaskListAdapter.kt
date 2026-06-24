package com.example.todotasks.ui.subTask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todotasks.R
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.ui.subTask.SubtaskItemCallbacks

class SubTaskListAdapter(
    private val callbacks: SubtaskItemCallbacks
) : ListAdapter<SubTask, SubTaskViewHolder>(SubTaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return SubTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubTaskViewHolder, position: Int) {
        holder.render(getItem(position), callbacks)
    }
}