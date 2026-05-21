package com.example.todotasks.ui.task

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.data.core.NotificationConfig.Companion.CHANNEL_TASK
import com.example.todotasks.databinding.ActivityTaskBinding
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.subTask.SubTaskActivity
import com.example.todotasks.ui.subTask.SubTaskActivity.Companion.EXTRA_TASK_ID
import com.example.todotasks.ui.subTask.SubTaskActivity.Companion.EXTRA_TASK_NAME
import com.example.todotasks.ui.task.adapter.TaskListAdapter
import com.example.todotasks.ui.task.Dialog.DialogDeleteTask
import com.example.todotasks.ui.task.Dialog.DialogTask
import com.example.todotasks.ui.task.model.TaskUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var rvAdapter: TaskListAdapter
    private val tasksViewModel: TaskViewModel by viewModels()
    private var toast: Toast? = null

    private val callbacks: TaskItemCallbacks by lazy {
        TaskItemCallbacks(
            editTask = { id, name, priority, date -> editTaskDialog(id, name, priority, date) },
            openSubTaskActivity = { id, name -> openSubTaskById(id, name) },
            deleteTask = { task -> openDialogDeleteTask(task) },
            updateCompletedTask = { id, completed -> updateCompletedTask(id, completed) }
        )
    }

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

        binding.rgTaskFilter.setOnCheckedChangeListener { _, checkedId ->
            val filter = when (checkedId) {
                R.id.rbAll -> TaskFilter.ALL
                R.id.rbCompletedTasks -> TaskFilter.COMPLETED
                R.id.rbNotCompletedTasks -> TaskFilter.NOT_COMPLETED
                else -> TaskFilter.ALL
            }
            tasksViewModel.setTaskFilter (filter)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

    }

    private fun setFlows() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tasksViewModel.taskUiState.collect { tasks -> updateTasks(tasks) }
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

    private fun editTaskDialog(id: Long, task: String, priority: TaskPriority, date: LocalDate?) {
        DialogTask(id, task, priority, date).show(supportFragmentManager, "")
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

    private fun openDialogDeleteTask(task: Task) {
        DialogDeleteTask(task).show(supportFragmentManager, "")
    }

    private fun updateCompletedTask(id: Long, isCompleted: Boolean) {
        tasksViewModel.updateCompletedTask(id, isCompleted)
    }

    private fun setRvAdapter() {
        rvAdapter = TaskListAdapter(
            callbacks
        )
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvTasks)

    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0, /*ItemTouchHelper.LEFT or*/ ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false // No necesitamos drag & drop
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            // Llama a tu adaptador para eliminar el item
            // adapter.notifyItemRemoved(position)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            // Aplica la animación nativa de Material Design al deslizar
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

}