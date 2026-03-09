package com.example.todotasks.ui.task

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
import com.example.todotasks.databinding.ActivityTaskBinding
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.subTask.SubTaskActivity
import com.example.todotasks.ui.subTask.SubTaskActivity.Companion.EXTRA_TASK_ID
import com.example.todotasks.ui.subTask.SubTaskActivity.Companion.EXTRA_TASK_NAME
import com.example.todotasks.ui.task.adapter.TaskListAdapter
import com.example.todotasks.ui.task.Dialog.DialogDeleteTask
import com.example.todotasks.ui.task.Dialog.DialogTask
import com.example.todotasks.ui.task.model.TaskUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var rvAdapter: TaskListAdapter
    private val tasksViewModel: TaskViewModel by viewModels()
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setRvAdapter()
        setFlows()

    }

    private fun setListeners() {

        binding.fabTask.setOnClickListener {
            newTaskDialog()
        }

    }

    private fun setFlows() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tasksViewModel.listaTasks.collect { tasks -> updateTasks(tasks) }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tasksViewModel.taskState.collect { taskState ->
                    when (taskState) {
                        UiState.Loading -> binding.pbLoading.visibility = View.VISIBLE
                        is UiState.Success -> {
                            showToast(taskState.message)
                        }

                        is UiState.Error -> {
                            showToast(taskState.message)
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

    private fun newTaskDialog() {
        DialogTask().show(supportFragmentManager, "")
    }

    private fun editTaskDialog(id: Long, task: String) {
        DialogTask(id, task).show(supportFragmentManager, "")
    }

    private fun updateTasks(tasks: List<TaskUI>) {
        rvAdapter.submitList(tasks)
    }

    private fun openSubTaskById(id: Long, task: String) {

        val intent = Intent(this, SubTaskActivity::class.java)
        intent.putExtra(EXTRA_TASK_ID, id)
        intent.putExtra(EXTRA_TASK_NAME, task)
        this.startActivity(intent)
    }

    private fun openDialogDeleteTask(id: Long, task: String) {
        DialogDeleteTask(id, task).show(supportFragmentManager, "")
    }

    private fun updateCompletedTask(id: Long, isCompleted: Boolean){
        tasksViewModel.updateCompletedTask(id, isCompleted)
    }

    private fun setRvAdapter() {

        rvAdapter = TaskListAdapter(
            editTask = ::editTaskDialog,
            openSubTaskActivity = ::openSubTaskById,
            deleteTask = ::openDialogDeleteTask,
            updateCompletedTask = ::updateCompletedTask
        )
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }

    }

}