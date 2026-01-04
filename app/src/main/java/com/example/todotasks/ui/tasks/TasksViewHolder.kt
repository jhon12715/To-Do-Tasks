package com.example.todotasks.ui.tasks

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.ui.tasks.model.TaskItem

class TasksViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val binding = ItemTaskBinding.bind(view)

    fun render(item: TaskItem){
        binding.tvTaskName.text = item.texto
        binding.cbTask.isChecked = item.completada
    }
}