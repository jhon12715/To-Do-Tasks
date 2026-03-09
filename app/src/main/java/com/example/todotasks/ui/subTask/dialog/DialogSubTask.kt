package com.example.todotasks.ui.subTask.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.databinding.DialogTasksBinding
import com.example.todotasks.ui.subTask.SubTaskViewModel
import com.example.todotasks.ui.task.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DialogSubTask(
    private val idSubTask: Long = 0,
    private val idTask: Long = 0,
    private val subTaskName: String = ""
) :
    DialogFragment() {

    private lateinit var binding: DialogTasksBinding
    private val viewModel: SubTaskViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogTasksBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        startUI()
        setFlows()
        setListeners()

        return builder.create()
    }

    private fun startUI() {
        if (idSubTask == 0L) {
            binding.tvTittle.text = "Añadir Subtarea"
        } else {
            binding.tvTittle.text = "Editar Subtarea"
            binding.etTask.setText(subTaskName)
        }
    }

    private fun setFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subTaskState.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            dismiss()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.btnFinish.setOnClickListener {
            val subTaskName = binding.etTask.text.toString().trim()
            if (subTaskName.isNotEmpty()) {
                if (idSubTask == 0L) {
                    viewModel.addSubTask(idTask, subTaskName)

                } else {
                    viewModel.updateSubTaskName(idSubTask, subTaskName)
                }
            }
        }
    }


}