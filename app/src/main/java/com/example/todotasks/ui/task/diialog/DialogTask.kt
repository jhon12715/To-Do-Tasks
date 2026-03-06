package com.example.todotasks.ui.task.diialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.databinding.DialogTasksBinding
import com.example.todotasks.ui.task.TaskViewModel
import com.example.todotasks.ui.task.UiState
import kotlinx.coroutines.launch

class DialogTask(val id: Long = 0, val taskName: String = "") : DialogFragment() {

    private lateinit var binding: DialogTasksBinding
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogTasksBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        println("id: $id")

        titulo()

        setListeners()
        setFlows()

        return builder.create()
    }

    private fun titulo() {
        binding.tvTittle.text = if (id == 0L) {
            "Nuevo"
        } else {
            binding.etTask.setText(taskName)
            "Editar"
        }
    }

    private fun setListeners() {

        binding.btnFinish.setOnClickListener {
            val taskName = binding.etTask.text.toString().trim()
            if (taskName != this.taskName) {
                if (taskName.isNotEmpty()) {

                    if (id == 0L) {
                        viewModel.insertTask(taskName)
                    } else {
                        viewModel.updateTask(id, taskName)
                    }

                }
            } else {
                dismiss()
            }
        }

    }

    private fun setFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskState.collect { state ->
                    when (state) {
                        is UiState.Success -> dismiss()
                        else -> {}
                    }

                }
            }
        }
    }
}