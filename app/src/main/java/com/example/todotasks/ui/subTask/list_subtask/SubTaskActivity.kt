package com.example.todotasks.ui.subTask.list_subtask

import android.content.Context
import android.content.Intent
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
import com.example.todotasks.domain.model.TaskIsCompletedFilter
import com.example.todotasks.ui.core.extensions.backgroundMoreWhite
import com.example.todotasks.ui.model.SubTaskUI
import com.example.todotasks.ui.subTask.SubTaskUiEvent
import com.example.todotasks.ui.subTask.SubtaskItemCallbacks
import com.example.todotasks.ui.subTask.list_subtask.adapter.SubTaskListAdapter
import com.example.todotasks.ui.subTask.dialog.DialogDeleteSubtask
import com.example.todotasks.ui.subTask.subtask_form.DialogSubTaskForm
import com.example.todotasks.ui.subTask.toUI
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

        fun newIntent(context: Context, taskId: Long, taskName: String) =
            Intent(context, SubTaskActivity::class.java).apply {
                putExtra(EXTRA_TASK_ID, taskId)
                putExtra(EXTRA_TASK_NAME, taskName)
            }

    }

    private lateinit var binding: ActivitySubtaskBinding
    private lateinit var rvAdapter: SubTaskListAdapter
    private lateinit var subTaskForm: DialogSubTaskForm
    private var toast: Toast? = null
    private var taskId: Long = 0L

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
        taskId = intent.getLongExtra(EXTRA_TASK_ID, -1L)



        viewModel.init(taskId, taskName)
        initUI(taskName)
        setListeners()
        setFlows()
        setAdapter()
    }

    private fun initUI(taskName: String) = with(binding) {
        tvTaskName.text = taskName
        clParent.backgroundMoreWhite(0.9f)
    }

    private fun setListeners() = with(binding) {
        fabTask.setOnClickListener {
            newSubtaskForm()
        }

        rgTaskFilter.setOnCheckedChangeListener { _, checkedId ->
            val filter = when (checkedId) {
                R.id.rbAll -> TaskIsCompletedFilter.ALL
                R.id.rbCompletedTasks -> TaskIsCompletedFilter.COMPLETED
                R.id.rbNotCompletedTasks -> TaskIsCompletedFilter.NOT_COMPLETED
                else -> TaskIsCompletedFilter.ALL
            }
            viewModel.onEvent(SubTaskUiEvent.UpdateFilterSubTaskCompleted(filter))
        }
    }

    private fun editSubtaskForm(subTask: SubTaskUI) {
        subTaskForm = DialogSubTaskForm.newInstance(subTask)
        openSubTaskDialog()
    }

    private fun newSubtaskForm() {
        subTaskForm = DialogSubTaskForm.newInstance(SubTaskUI(idTask = taskId))
        openSubTaskDialog()
    }

    private fun openSubTaskDialog() {
        subTaskForm.show(supportFragmentManager, "")
    }

    private fun deleteSubtaskDialog(idTask: Long, text: String) {
        DialogDeleteSubtask.newInstance(idTask, text).show(supportFragmentManager, "")
    }

    private fun setFlows() {

        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.subTaskUiState.map { it.listSubtasks }
                        .distinctUntilChanged()
                        .collect { subTaskList ->
                            rvAdapter.submitList(subTaskList)
                        }


                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.subTaskUiState.map { it.subTaskCompletedText }
                        .distinctUntilChanged()
                        .collect { subTaskCompleted ->
                            binding.tvSubTasksCompleted.text = subTaskCompleted
                        }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.subTaskEventResult
                        .collect { eventResult ->
                            showToast(eventResult.message)
                        }
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