package com.example.todotasks.ui.subTask.dialog

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.databinding.DialogFormTaskBinding
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.subTask.SubTaskUiEvent
import com.example.todotasks.ui.subTask.SubTaskViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DialogSubTask() :
    DialogFragment() {

    private var _binding: DialogFormTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SubTaskViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFormTaskBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        startUI()
        setFlows()
        setListeners()
        initSpinner()

        return builder.create()
    }

    private fun startUI() {
        val subTaskForm = viewModel.subTaskFormState.value
        if (subTaskForm.subTaskId == 0L) {
            binding.tvTittle.text = "Añadir Subtarea"
        } else {
            viewModel.onEvent(SubTaskUiEvent.ChangeSubTaskPriorityForm(subTaskForm.priorityForm))
            binding.tvTittle.text = "Editar Subtarea"
            binding.etTask.setText(subTaskForm.subTasknameForm)
        }

        binding.tvDateTittle.visibility = View.GONE
        binding.tvDate.visibility = View.GONE
        binding.ivDate.visibility = View.GONE
        binding.tvTaskCategory.visibility = View.GONE
        binding.tilCategory.visibility = View.GONE

    }

    private fun setFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subTaskEventResult.collect { eventResult ->
                    when (eventResult) {
                        is ResultEvent.Success -> {
                            dismiss()
                        }

                        else -> {}
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subTaskFormState.map { it.isValid }
                    .debounce { 50 }
                    .distinctUntilChanged()
                    .collect { isValid ->

                        binding.btnFinish.isClickable = isValid


                        if (isValid) {

                            binding.btnFinish.text = "Valido"
                            binding.btnFinish.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    com.example.todotasks.R.color.formValid
                                )
                            )

                        } else {

                            binding.btnFinish.text = "No Valido"
                            binding.btnFinish.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    com.example.todotasks.R.color.formNotValid
                                )
                            )
                        }

                    }
            }
        }


    }

    private fun setListeners() {
        binding.btnFinish.setOnClickListener {
            viewModel.onEvent(SubTaskUiEvent.UpsertSubTask)
        }

        binding.etTask.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.onEvent(
                    SubTaskUiEvent.ChangeSubTaskNameForm(s.toString())
                )
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }

        }

        )

    }

    private fun initSpinner() {

        val priorities = TaskPriority.values()

        println("priorities: $priorities")

        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, priorities)

        binding.autoCompletePriorityTasktv.apply {
            setAdapter(arrayAdapter)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.onEvent(SubTaskUiEvent.ChangeSubTaskPriorityForm(priorities[position]))
            }
            val priorityName = viewModel.subTaskFormState.value.priorityForm.name
            setText(priorityName, false)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onEvent(SubTaskUiEvent.CloseForm)
        _binding = null
    }

}