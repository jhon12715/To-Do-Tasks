package com.example.todotasks.ui.task.task_form

import android.R
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.databinding.DialogFormTaskBinding
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.core.extensions.backgroundMoreWhite
import com.example.todotasks.ui.core.extensions.cardbackgroundMoreWhite
import com.example.todotasks.ui.core.extensions.getParcelableCompat
import com.example.todotasks.ui.core.extensions.hideKeyboard
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.TaskUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class DialogTaskForm() : DialogFragment() {

    private var _binding: DialogFormTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskFormViewModel by viewModels()

    private val taskForm get() = viewModel.taskFormState.value

    companion object {
        private const val TASK_ITEM = "TASK_ITEM"

        fun newInstance(task: TaskUI): DialogTaskForm {
            val dialog = DialogTaskForm()
            val bundle = Bundle().apply {
                putParcelable(TASK_ITEM, task)
            }
            dialog.arguments = bundle

            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.run {
            val task = getParcelableCompat<TaskUI>(TASK_ITEM) ?: TaskUI()
            viewModel.onEvent(TaskFormEvent.OpeningForm(task))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFormTaskBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        setListeners()
        setFlows()


        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawableResource(com.example.todotasks.R.drawable.bg_dialog)
            setLayout(
                (resources.displayMetrics.widthPixels * 0.8).toInt(), // ancho 80%
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun initDownMenu() {
        initPriorityTaskDownMenu()
    }

    private fun initPriorityTaskDownMenu() {
        val priorities = TaskPriority.values()
        val stringPriorities = priorities.map { it.name }

        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, stringPriorities)
        val taskPriority = viewModel.taskFormState.value.priority
        binding.autoCompletePriorityTasktv.apply {
            setAdapter(arrayAdapter)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.onEvent(TaskFormEvent.UpdatePriorityTaskForm(priorities[position]))
            }



            setText(priorities.first { it == taskPriority }.name, false)


        }
    }

    private fun initCategoryTaskDownMenu() {

        val categoryId: Long = taskForm.categoryId ?: -1L
        println("idd: $categoryId")
        val categories = taskForm.availableCategories
        val stringsCategories: List<String> =
            categories.map { category -> category.name }

        println("strings: $stringsCategories")


        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, stringsCategories)

        binding.autoCompleteCategorytv.apply {
            setAdapter(arrayAdapter)
            threshold = 1
            setOnItemClickListener { _, _, position, _ ->
                val idCategorySelected = categories[position].id
                println("category id: $idCategorySelected")
                viewModel.onEvent(TaskFormEvent.UpdateCategoryIdTaskForm(idCategorySelected))
            }
            println("categories: ${taskForm.availableCategories}")
            setText(categories.first { it.id == categoryId }.name, false)

        }
    }

    private fun initUI() {
        binding.parent.cardbackgroundMoreWhite(0.9f)
        val tittleModeForm = if (taskForm.id == 0L) "Nuevo" else "Editar"
        binding.tvTittle.text = tittleModeForm
        binding.etTask.setText(taskForm.name)
        editDateFormUI(taskForm.date)
        initDownMenu()

    }

    private fun setListeners() = with(binding) {

        btnFinish.setOnClickListener {
            viewModel.onEvent(TaskFormEvent.UpsertTaskForm)
        }

        ivDate.setOnClickListener { showDatePicker() }

        ivDelete.setOnClickListener {
            viewModel.onEvent(
                TaskFormEvent.UpdateDateTaskForm(
                    null
                )
            )
        }

        etTask.setOnFocusChangeListener { view, _ ->
            view.hideKeyboard()
        }

        etTask.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.onEvent(
                    TaskFormEvent.UpdateNameTaskForm(
                        s.toString()
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

                launch {
                    viewModel.resultEvent
                        .collect { resultEvent ->
                            when (resultEvent) {
                                is ResultEvent.Error -> {}
                                is ResultEvent.Success -> dismiss()
                            }
                        }
                }

                launch {
                    viewModel.taskFormState.map { it.isReady }
                        .distinctUntilChanged()
                        .collect {
                            initUI()

                        }
                }

                launch {
                    viewModel.taskFormState.map { it.date }
                        .distinctUntilChanged()
                        .collect { date ->
                            editDateFormUI(date)
                        }
                }

                launch {
                    viewModel.taskFormState.map { it.availableCategories }
                        .distinctUntilChanged()
                        .collect { categories ->
                            if (categories.isNotEmpty()) {
                                initCategoryTaskDownMenu()
                            }
                        }
                }

                launch {
                    viewModel.taskFormState.map { it.isValid }
                        .debounce { 100 }
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

        val today = taskForm.date ?: LocalDate.now()
        // Ponerle mínimo
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                viewModel.onEvent(TaskFormEvent.UpdateDateTaskForm(selectedDate))
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