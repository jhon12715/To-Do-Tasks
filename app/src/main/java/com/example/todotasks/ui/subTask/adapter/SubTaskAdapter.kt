package com.example.todotasks.ui.subTask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task

class SubTaskAdapter(
    var lista: List<SubTask> = mutableListOf(),
    private val updateSubTaskCompleted: (Long, Boolean) -> Unit,
    private val updateSubTaskName: (Long, String) -> Unit,
    private val deleteSubtask: (Long, String) -> Unit
) : RecyclerView.Adapter<SubTaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return SubTaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: SubTaskViewHolder, position: Int) {
        holder.render(lista[position], updateSubTaskCompleted, updateSubTaskName, deleteSubtask)
    }

}