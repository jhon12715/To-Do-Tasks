package com.example.todotasks.ui.task.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.todotasks.databinding.DialogDeleteTaskBinding
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.task.TaskViewModel

class DialogDeleteTask(
    private val task: Task
) : DialogFragment() {

    private lateinit var binding: DialogDeleteTaskBinding
    private val viewModel: TaskViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteTaskBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        startUI()
        setListeners()

        return builder.create()
    }

    private fun startUI() {
        binding.tvTittle.text = "Eliminar Tarea"
        binding.tvMessage.text = "seguro que desea borrar la tarea ${task.task}"


    }

    private fun setListeners() {
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnAccept.setOnClickListener {
             //viewModel.deleteTask(task)
            dismiss()
        }
    }
}