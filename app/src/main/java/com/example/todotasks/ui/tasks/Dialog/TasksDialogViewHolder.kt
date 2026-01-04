package com.example.todotasks.ui.tasks.Dialog

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.databinding.ItemDialogTasksBinding
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.ui.tasks.model.TaskItem

class TasksDialogViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val binding = ItemDialogTasksBinding.bind(view)

    fun render(subTask: String){
        binding.tvTaskName.text = subTask
        println("entro $subTask")
        //binding.cbTask.isChecked = item.completada
    }
}