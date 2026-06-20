package com.example.todotasks.ui.subTask

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todotasks.R
import com.example.todotasks.databinding.ActivitySubtaskBinding
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.subTask.adapter.SubTaskListAdapter
import com.example.todotasks.ui.subTask.dialog.DialogDeleteSubtask
import com.example.todotasks.ui.subTask.dialog.DialogSubTask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubTaskActivity : AppCompatActivity() {

    private val viewModel: SubTaskViewModel by viewModels()

    companion object {
        const val EXTRA_TASK_ID = "EXTRA_TASK_ID"
        const val EXTRA_TASK_NAME = "EXTRA_TASK_NAME"
    }

    private lateinit var binding: ActivitySubtaskBinding
    private lateinit var rvAdapter: SubTaskListAdapter
    private var toast: Toast? = null

    private val callbacks: SubtaskItemCallbacks by lazy {
        SubtaskItemCallbacks(
            updateSubTaskCompleted = { id, isCompleted -> updateSubTaskCompleted(id, isCompleted) },
            editSubTaskForm = { subTask -> editSubtaskForm(subTask) },
            deleteSubtask = { id, name -> deleteSubtaskDialog(id, name) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubtaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskName: String = intent.getStringExtra(EXTRA_TASK_NAME) ?: ""
        val idTask: Long = intent.getLongExtra(EXTRA_TASK_ID, -1L)



        viewModel.init(idTask, taskName)
        startUI(taskName)
        setListeners()
        setFlows()
        setAdapter()
    }

    private fun startUI(taskName: String) {
        binding.tvTaskName.text = taskName
    }

    private fun setListeners() {
        binding.fabTask.setOnClickListener {
            newSubtaskForm()
        }

        binding.rgTaskFilter.setOnCheckedChangeListener { _, checkedId ->
            val filter = when (checkedId) {
                R.id.rbAll -> TaskFilter.ALL
                R.id.rbCompletedTasks -> TaskFilter.COMPLETED
                R.id.rbNotCompletedTasks -> TaskFilter.NOT_COMPLETED
                else -> TaskFilter.ALL
            }
            viewModel.onEvent(SubTaskUiEvent.UpdateFilterSubTaskCompleted(filter))
        }
    }

    private fun editSubtaskForm(subTask: SubTask){
        viewModel.onEvent(SubTaskUiEvent.EditSubTaskForm(subTask))
    }

    private fun newSubtaskForm(){
        viewModel.onEvent(SubTaskUiEvent.CreateNewSubTaskForm)
    }

    private fun openSubTaskDialog() {
        DialogSubTask().show(supportFragmentManager, "")
    }

    private fun deleteSubtaskDialog(idTask: Long, text: String) {
        DialogDeleteSubtask(idTask, text).show(supportFragmentManager, "")
    }

    private fun setFlows() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subTaskUi
                    .collect { subTaskUi ->
                        binding.tvSubTasksCompleted.text = subTaskUi.textSubTasksCompleted
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subTaskUiState.map { it.listSubtasks }
                    .distinctUntilChanged()
                    .collect { subTaskList ->
                        rvAdapter.submitList(subTaskList)
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subTaskUiState.map { it.isFormVisible }
                    .distinctUntilChanged()
                    .collect { isFormVisible ->
                        if (isFormVisible){
                            openSubTaskDialog()
                        }
                    /*when (subTaskState) {
                        ResultEvent.Loading -> binding.pbLoading.visibility = View.VISIBLE
                        is com.example.todotasks.ui.task.UiState.Result.Success -> {
                            showToast(subTaskState.message)
                            binding.pbLoading.visibility = View.GONE
                        }

                        is com.example.todotasks.ui.task.UiState.Result.Error -> {
                            showToast(subTaskState.message)
                            binding.pbLoading.visibility = View.GONE
                        }
                    }*/
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subTaskEventResult
                    .collect { eventResult ->
                        showToast(eventResult.message)
                    }
            }
        }

    }

    private fun showToast(message: String) {
        binding.pbLoading.visibility = View.GONE
        toast?.cancel()
        toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        toast?.show()
    }

    private fun updateSubTaskCompleted(id: Long, isChecked: Boolean) {
        viewModel.onEvent(SubTaskUiEvent.UpdateSubTaskCompleted(id, isChecked))
    }

    private fun setAdapter() {
        rvAdapter = SubTaskListAdapter(
            callbacks
        )
        binding.rvSubTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }
    }

}