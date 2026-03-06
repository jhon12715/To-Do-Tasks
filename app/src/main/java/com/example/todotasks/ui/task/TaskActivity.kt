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
import com.example.todotasks.ui.task.adapter.TaskAdapter
import com.example.todotasks.ui.task.diialog.DialogDeleteTask
import com.example.todotasks.ui.task.diialog.DialogTask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private var lista: MutableList<Task> = mutableListOf()
    private lateinit var rvAdapter: TaskAdapter
    private val tasksViewModel: TaskViewModel by viewModels()
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setRvAdapter()
        setFlows()
        getAllTasks()

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

    private fun getAllTasks() {
        tasksViewModel.getAllTasks()
    }

    private fun newTaskDialog() {
        DialogTask().show(supportFragmentManager, "")
    }

    private fun editTaskDialog(task: Task) {
        val id = task.id
        val taskName = task.task
        DialogTask(id, taskName).show(supportFragmentManager, "")
    }

    private fun updateTasks(tasks: List<Task>) {
        rvAdapter.lista = tasks
        rvAdapter.notifyDataSetChanged()
    }

    private fun openSubTaskById(task: Task) {

        val intent = Intent(this, SubTaskActivity::class.java)
        intent.putExtra(EXTRA_TASK_ID, task.id)
        intent.putExtra(EXTRA_TASK_NAME, task.task)
        this.startActivity(intent)
    }

    private fun setRvAdapter() {

        rvAdapter = TaskAdapter(
            editTask = ::editTaskDialog,
            openSubTaskActivity = ::openSubTaskById,
            deleteTask = ::openDialogDeleteTask
        )
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }

    }

    private fun openDialogDeleteTask(task: Task) {
        DialogDeleteTask(task).show(supportFragmentManager, "")
    }

}