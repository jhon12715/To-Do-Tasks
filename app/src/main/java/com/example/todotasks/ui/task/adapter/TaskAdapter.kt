package com.example.todotasks.ui.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.task.model.TaskUI

class TaskAdapter(
    private val lista: List<TaskUI> = listOf(),
    private val editTask: (Long, String) -> Unit,
    private val openSubTaskActivity: (Long, String) -> Unit,
    private val deleteTask: (Long, String) -> Unit,
    private val updateCompletedTask: (Long, Boolean) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder, position: Int
    ) {
        holder.render(lista[position], editTask, openSubTaskActivity, deleteTask, updateCompletedTask)
    }

}