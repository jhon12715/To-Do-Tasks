package com.example.todotasks.ui.tasks.Dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.ui.tasks.TasksViewHolder
import com.example.todotasks.ui.tasks.model.TaskItem

class TasksDialogAdapter(var lista: MutableList<String> = mutableListOf()): RecyclerView.Adapter<TasksDialogViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksDialogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog_tasks, parent, false)
        return TasksDialogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: TasksDialogViewHolder, position: Int) {
        holder.render(lista[position])
    }

}