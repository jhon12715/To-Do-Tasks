package com.example.todotasks.ui.subTask.subtask_form

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.databinding.DialogFormTaskBinding
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.core.extensions.applyWindowApparence
import com.example.todotasks.ui.core.extensions.cardbackgroundMoreWhite
import com.example.todotasks.ui.core.extensions.getParcelableCompat
import com.example.todotasks.ui.model.SubTaskUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DialogSubTaskForm() :
    DialogFragment() {

    private var _binding: DialogFormTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SubTaskFormViewModel by viewModels()

    private var toast: Toast? = null

    companion object {
        const val SUBTASK_ITEM = "SUBTASK_ITEM"
        fun newInstance(subTask: SubTaskUI = SubTaskUI()): DialogSubTaskForm =
            DialogSubTaskForm().apply {
                arguments = Bundle().apply {
                    putParcelable(SUBTASK_ITEM, subTask)
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFormTaskBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val subTask = arguments?.getParcelableCompat<SubTaskUI>(SUBTASK_ITEM) ?: SubTaskUI()
        viewModel.onEvent(SubTaskFormEvent.OpeningForm(subTask))
        startUI(subTask)
        setFlows()
        setListeners()

        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        applyWindowApparence()
        initSpinner()
    }

    private fun startUI(subTask: SubTaskUI) {
        binding.parent.cardbackgroundMoreWhite(0.9f)
        if (subTask.id == 0L) {
            binding.tvTittle.text = "Añadir Subtarea"
        } else {
            binding.tvTittle.text = "Editar Subtarea"
            binding.etTask.setText(subTask.title)
        }

        binding.tvDateTittle.visibility = View.GONE
        binding.tvDate.visibility = View.GONE
        binding.ivDate.visibility = View.GONE
        binding.tilCategory.visibility = View.GONE

    }

    private fun setFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultEvent.collect { eventResult ->

                    showToast(eventResult.message)

                    when (eventResult) {
                        is ResultEvent.Success -> {
                            dismiss()
                        }

                        else -> {

                        }
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
            viewModel.onEvent(SubTaskFormEvent.CommitForm)
        }

        binding.etTask.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.onEvent(
                    SubTaskFormEvent.UpdateSubTaskName(s.toString())
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

        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, priorities)

        binding.autoCompletePriorityTasktv.apply {
            setAdapter(arrayAdapter)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.onEvent(SubTaskFormEvent.UpdateSubTaskPriority(priorities[position]))
            }
            val priorityName = viewModel.subTaskFormState.value.prioritySubTask.name
            setText(priorityName, false)

        }
    }

    private fun showToast(message: String) {
        val context = binding.root.context
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast!!.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}