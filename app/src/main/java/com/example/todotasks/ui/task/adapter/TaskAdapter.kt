package com.example.todotasks.ui.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.task.task_list.adapter.TaskItemCallbacks
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.task.task_list.adapter.TaskViewHolder

class TaskAdapter(
    private val lista: List<TaskUI> = listOf(),
    private val callbacks: TaskItemCallbacks
) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder, position: Int
    ) {
        holder.render(lista[position], ScreenMode.NORMAL, callbacks)
    }

}