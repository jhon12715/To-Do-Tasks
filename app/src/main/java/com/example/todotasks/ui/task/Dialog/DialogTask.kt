package com.example.todotasks.ui.task.Dialog

import android.R
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.databinding.DialogTasksBinding
import com.example.todotasks.ui.task.TaskViewModel
import com.example.todotasks.ui.task.UiState
import com.example.todotasks.domain.model.TaskPriority
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DialogTask(val id: Long = 0, private val taskName: String = "", private val priority: TaskPriority = TaskPriority.NORMAL, private val date: LocalDate? = null) : DialogFragment() {

    private lateinit var binding: DialogTasksBinding
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogTasksBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        startUI()
        setListeners()
        setFlows()
        initSpinner()

        return builder.create()
    }

    private fun startUI() {
        binding.tvTittle.text = if (id == 0L) {
            "Nuevo"
        } else {
            binding.etTask.setText(taskName)
            viewModel.setDate(date)
            "Editar"
        }
        viewModel.updateTaskPriority(priority)
    }

    private fun setListeners() {

        binding.btnFinish.setOnClickListener {
            val newTaskName = binding.etTask.text.toString()
            val newDate: LocalDate? = viewModel.date.value
            val newPriority = viewModel.taskPriority.value

                    if (id == 0L) {
                        viewModel.insertTask(newTaskName, newPriority, newDate)
                    } else {
                        viewModel.updateTask(id, newTaskName,taskName, newPriority, priority, newDate, date)
                    }
        }

        binding.ivDate.setOnClickListener { showDatePicker() }

        binding.ivDelete.setOnClickListener { viewModel.setDate(null) }

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.date.collect { date ->

                    if (date == null) {
                        binding.tvDate.text = "Sin fecha límite"
                        binding.ivDelete.visibility = View.GONE
                    }else{
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        binding.tvDate.text = date.format(formatter)
                        binding.ivDelete.visibility = View.VISIBLE
                    }

                }
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

        val currentFilter = viewModel.taskPriority.value
        println("current: $currentFilter")
        spinner.setSelection(priority.indexOf(currentFilter))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

                val selectedPriority = priority[position]          // enum real
                viewModel.updateTaskPriority(selectedPriority)         // actualizar StateFlow
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showDatePicker() {
        val today = date ?: LocalDate.now()
        // Ponerle mínimo
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                viewModel.setDate(selectedDate)
            },
            today.year,
            today.monthValue - 1,
            today.dayOfMonth
        )
        datePicker.show()
    }

}