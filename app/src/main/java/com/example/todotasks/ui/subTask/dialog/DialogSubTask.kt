package com.example.todotasks.ui.subTask.dialog

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.databinding.DialogTasksBinding
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.subTask.SubTaskViewModel
import com.example.todotasks.ui.task.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DialogSubTask(
    private val idSubTask: Long = 0,
    private val idTask: Long = 0,
    private val subTaskName: String = "",
    private val priority: TaskPriority = TaskPriority.NORMAL
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
        initSpinner()

        return builder.create()
    }

    private fun startUI() {
        if (idSubTask == 0L) {
            binding.tvTittle.text = "Añadir Subtarea"
        } else {
            viewModel.updateSubtaskPriority(priority)
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
            val newSubTaskName = binding.etTask.text.toString()
            val newPriority = viewModel.priority.value

                if (idSubTask == 0L) {
                    viewModel.insertSubTask(idTask, newSubTaskName, priority)

                } else {
                    viewModel.updateSubTask(idSubTask, newSubTaskName, subTaskName, newPriority, priority)
                }

        }
    }

    private fun initSpinner() {
        val spinner = binding.spinnerPriorityTask
        val priority = TaskPriority.values()

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            priority
        ) // o displayName si lo tienes
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val currentFilter = viewModel.priority.value
        spinner.setSelection(priority.indexOf(currentFilter))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

                val selectedPriority = priority[position]          // enum real
                viewModel.updateSubtaskPriority(selectedPriority)         // actualizar StateFlow
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

}