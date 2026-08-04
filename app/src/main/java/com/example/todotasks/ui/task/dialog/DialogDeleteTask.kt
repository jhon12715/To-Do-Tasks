package com.example.todotasks.ui.task.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.todotasks.R
import com.example.todotasks.databinding.DialogDeleteTaskBinding
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.core.extensions.applyWindowApparence
import com.example.todotasks.ui.core.extensions.backgroundMoreWhite
import com.example.todotasks.ui.task.task_list.TaskListViewModel

class DialogDeleteTask(
    private val task: Task
) : DialogFragment() {

    private lateinit var binding: DialogDeleteTaskBinding
    private val viewModel: TaskListViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteTaskBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        startUI()
        setListeners()

        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        binding.clParent.backgroundMoreWhite(0.9f)
        applyWindowApparence()
    }

    private fun startUI() {
        binding.tvTittle.text = "Eliminar Tarea"
        binding.tvMessage.text = "seguro que desea borrar la tarea: ${task.task}"

    }

    private fun setListeners() {
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnAccept.setOnClickListener {
            //viewModel.deleteTask(task)
            dismiss()
        }
    }
}