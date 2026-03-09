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
import com.example.todotasks.databinding.ActivitySubtaskBinding
import com.example.todotasks.databinding.ActivityTaskBinding
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.ui.subTask.adapter.SubTaskAdapter
import com.example.todotasks.ui.subTask.adapter.SubTaskListAdapter
import com.example.todotasks.ui.subTask.dialog.DialogDeleteSubtask
import com.example.todotasks.ui.subTask.dialog.DialogSubTask
import com.example.todotasks.ui.task.UiState
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubtaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskName: String = intent.getStringExtra(EXTRA_TASK_NAME) ?: ""
        val idTask: Long = intent.getLongExtra(EXTRA_TASK_ID, -1L)
        viewModel.setTaskId(idTask)
        startUI(taskName)
        setListeners(idTask)
        setFlows()
        setAdapter()
        tvSubTasksCompleted()
    }

    private fun startUI(taskName: String) {
        binding.tvTaskName.text = taskName
    }

    private fun setListeners(idTask: Long) {
        binding.fabTask.setOnClickListener {
            newSubTaskDialog(idTask)
        }
    }

    private fun newSubTaskDialog(idTask: Long) {
        DialogSubTask(idTask = idTask).show(supportFragmentManager, "")
    }

    private fun updateSubtaskDialog(idSubTask: Long, text: String) {
        DialogSubTask(idSubTask = idSubTask, subTaskName = text).show(supportFragmentManager, "")
    }

    private fun deleteSubtaskDialog(idTask: Long, text: String) {
        DialogDeleteSubtask(idTask, text).show(supportFragmentManager, "")
    }

    private fun setFlows() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listaSubTasks.collect { subTasks ->
                    rvAdapter.submitList(subTasks)
                    tvSubTasksCompleted()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subTaskState.collect { subTaskState ->
                    when (subTaskState) {
                        UiState.Loading -> binding.pbLoading.visibility = View.VISIBLE
                        is UiState.Success -> {
                            showToast(subTaskState.message)
                        }

                        is UiState.Error -> {
                            showToast(subTaskState.message)
                        }
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
        viewModel.updateSubTaskCompleted(id, isChecked)
    }

    private fun tvSubTasksCompleted() {
        binding.tvSubTasksCompleted.text = viewModel.getCompletedSubTasks()
    }

    private fun setAdapter() {
        rvAdapter = SubTaskListAdapter(
            updateSubTaskCompleted = ::updateSubTaskCompleted,
            updateSubTaskName = ::updateSubtaskDialog,
            deleteSubtask = ::deleteSubtaskDialog
        )
        binding.rvSubTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }
    }

}