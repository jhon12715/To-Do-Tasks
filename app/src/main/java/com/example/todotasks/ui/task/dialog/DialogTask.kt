package com.example.todotasks.ui.task.dialog

import android.R
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.databinding.DialogFormTaskBinding
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.task.TaskUiEvent
import com.example.todotasks.ui.task.TaskViewModel
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.subTask.SubTaskUiEvent
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DialogTask() : DialogFragment() {

    private var _binding: DialogFormTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by activityViewModels()

    private val taskFormState
        get() = viewModel.taskFormState.value

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFormTaskBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        initUI()
        setListeners()
        setFlows()


        return builder.create()
    }

    private fun initUI() {
        val tittleModeForm = if (taskFormState.id == 0L) "Nuevo" else "Editar"
        binding.tvTittle.text = tittleModeForm
        binding.etTask.setText(taskFormState.taskName)
        editDateFormUI(taskFormState.date)
        initDownMenu()
    }

    private fun initDownMenu() {
        initPriorityTaskDownMenu()
        initCategoryTaskDownMenu()
    }

    private fun initPriorityTaskDownMenu() {
        val priorities = TaskPriority.values()
        val stringPriorities = priorities.map { it.name }
        println("priorities: $stringPriorities")
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, stringPriorities)
        val taskPriority = taskFormState.priority
        binding.autoCompletePriorityTasktv.apply {
            setAdapter(arrayAdapter)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.onEvent(TaskUiEvent.UpdatePriorityTaskForm(priorities[position]))
            }



            setText(priorities.first { it == taskPriority }.name, false)


        }
    }

    private fun initCategoryTaskDownMenu() {

        val categoryId: Long = taskFormState.categoryId ?: -1L
        println("idd: $categoryId")
        val categories = taskFormState.availableCategories
        val stringsCategories: List<String> =
            categories.map { category -> category.name }

        println("strings: $stringsCategories")


        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, stringsCategories)

        binding.autoCompleteCategorytv.apply {
            setAdapter(arrayAdapter)
            setOnItemClickListener { _, _, position, _ ->
                val idCategorySelected = categories[position].id
                println("category id: $idCategorySelected")
                viewModel.onEvent(TaskUiEvent.UpdateCategoryIdTaskForm(idCategorySelected))
            }
            println("categories: ${taskFormState.availableCategories}")
            setText(categories.first { it.id == categoryId }.name, false)


        }
    }

    private fun setListeners() {
        binding.btnFinish.setOnClickListener {
            viewModel.onEvent(TaskUiEvent.UpsertTaskForm)
        }

        binding.ivDate.setOnClickListener { showDatePicker() }

        binding.ivDelete.setOnClickListener { viewModel.onEvent(TaskUiEvent.UpdateDateTaskForm(null)) }

        binding.etTask.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.onEvent(
                    TaskUiEvent.UpdateNameTaskForm(
                        s.toString(), viewModel.taskFormState.value.id
                    )
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

    private fun setFlows() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultEvent
                    .collect { resultEvent ->
                        when (resultEvent) {
                            is ResultEvent.Error -> {}
                            is ResultEvent.Success -> dismiss()
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskFormState.map { it.date }
                    .distinctUntilChanged()
                    .collect { date ->
                        editDateFormUI(date)
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskFormState.map { it.isValid }
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

    private fun editDateFormUI(newDate: LocalDate?) {
        if (newDate == null) {
            binding.tvDate.text = "Sin fecha límite"
            binding.ivDelete.visibility = View.GONE
        } else {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            binding.tvDate.text = newDate.format(formatter)
            binding.ivDelete.visibility = View.VISIBLE
        }
    }

    private fun editPriorityTaskFormUI(newPriority: TaskPriority) {

    }

    private fun editCategoryFormDownMenuUI(newCategoryFormId: Long) {
        //val categorySelected: Int = categories.indexOfFirst { category ->
        //   category.id == newCategoryFormId
        //}
        //binding.autoCompleteCategorytv.setSelection(categorySelected)
    }


    private fun showDatePicker() {

        val today = taskFormState.date ?: LocalDate.now()
        // Ponerle mínimo
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                viewModel.onEvent(TaskUiEvent.UpdateDateTaskForm(selectedDate))
            },
            today.year,
            today.monthValue - 1,
            today.dayOfMonth
        )
        datePicker.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //viewModel.onEvent(SubTaskUiEvent.CloseForm)
        _binding = null
    }
}