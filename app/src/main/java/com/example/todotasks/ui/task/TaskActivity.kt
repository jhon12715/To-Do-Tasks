package com.example.todotasks.ui.task

import android.content.Intent
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.databinding.ActivityTaskBinding
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.subTask.SubTaskActivity
import com.example.todotasks.ui.subTask.SubTaskActivity.Companion.EXTRA_TASK_ID
import com.example.todotasks.ui.subTask.SubTaskActivity.Companion.EXTRA_TASK_NAME
import com.example.todotasks.ui.task.adapter.TaskListAdapter
import com.example.todotasks.ui.task.dialog.DialogDeleteTask
import com.example.todotasks.ui.task.dialog.DialogTask
import com.example.todotasks.ui.task.adapter.CategoriesListAdapter
import com.example.todotasks.ui.task.dialog.CategoryTaskSheet
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var rvAdapter: TaskListAdapter
    private lateinit var rvCategoriesAdapter: CategoriesListAdapter
    private val viewModel: TaskViewModel by viewModels()
    private var toast: Toast? = null

    private val callbacks: TaskItemCallbacks by lazy {
        TaskItemCallbacks(
            editTask = { id, name, priority, date, categoryId ->
                openEditTaskDialog(
                    id,
                    name,
                    priority,
                    date,
                    categoryId
                )
            },
            openSubTaskActivity = { id, name -> openSubTaskById(id, name) },
            deleteTask = { task -> openDialogDeleteTask(task) },
            updateCompletedTask = { id, completed -> updateCompletedTask(id, completed) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var start = SystemClock.elapsedRealtime()
        super.onCreate(savedInstanceState)
        println("tiempo ahora 1: ${SystemClock.elapsedRealtime() - start} ms")
        start = SystemClock.elapsedRealtime()

        val vm = viewModel
        println("tiempo ahora 2: ${SystemClock.elapsedRealtime() - start} ms")
        start = SystemClock.elapsedRealtime()
        binding = ActivityTaskBinding.inflate(layoutInflater)
        println("tiempo ahora 3: ${SystemClock.elapsedRealtime() - start} ms")
        start = SystemClock.elapsedRealtime()
        setContentView(binding.root)
        println("tiempo ahora 4: ${SystemClock.elapsedRealtime() - start} ms")
        start = SystemClock.elapsedRealtime()
        setListeners()
        println("tiempo ahora 5: ${SystemClock.elapsedRealtime() - start} ms")
        start = SystemClock.elapsedRealtime()
        setRvAdapter()
        println("tiempo ahora 6: ${SystemClock.elapsedRealtime() - start} ms")
        start = SystemClock.elapsedRealtime()
        setRvCategoriesAdapter()
        println("tiempo ahora 7: ${SystemClock.elapsedRealtime() - start} ms")
        start = SystemClock.elapsedRealtime()
        setFlows()
        println("tiempo ahora 8: ${SystemClock.elapsedRealtime() - start} ms")
    }

    private fun setListeners() {

        binding.fabTask.setOnClickListener {
            openNewTaskDialog()
        }

        binding.rgTaskFilter.setOnCheckedChangeListener { _, checkedId ->
            val filter = when (checkedId) {
                R.id.rbAll -> TaskFilter.ALL
                R.id.rbCompletedTasks -> TaskFilter.COMPLETED
                R.id.rbNotCompletedTasks -> TaskFilter.NOT_COMPLETED
                else -> TaskFilter.ALL
            }
            viewModel.onEvent(TaskUiEvent.UpdateTaskFilter(filter))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

    }

    private fun setFlows() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskState.collect { tasks ->
                    updateTasks(tasks)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoriesState.collect { categories ->
                    updateCategories(categories)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskUIState.map { it.isLoading }
                    .collect { isLoading ->
                    binding.viewIsLoading.isVisible = isLoading
                }
            }
        }

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultEvent.collect { result ->

                        showToast(result.message)
                    }
            }

        }

    }

    private fun openAddCategoryDialog() {
        CategoryTaskSheet().show(supportFragmentManager, "")
    }

    private fun showToast(message: String) {
        binding.pbLoading.visibility = View.GONE
        toast?.cancel()
        toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        toast?.show()
    }

    private fun openEditTaskDialog(
        id: Long,
        name: String,
        priority: TaskPriority,
        date: LocalDate?,
        categoryId: Long?
    ) {
        viewModel.onEvent(
            TaskUiEvent.OpenEditTaskDialog(id, name, priority, date, categoryId)
        )
        DialogTask().show(supportFragmentManager, "")
    }

    private fun openNewTaskDialog() {
        viewModel.onEvent(
            TaskUiEvent.OpenNewTaskDialog
        )
        DialogTask().show(supportFragmentManager, "")
    }

    private fun updateTasks(tasks: List<TaskUI>) {
        var start = SystemClock.elapsedRealtime()
        rvAdapter.submitList(tasks)
        println("tiempo tasks: ${SystemClock.elapsedRealtime() - start} ms")
    }

    private fun updateCategories(categories: List<TypeCategoryUI>) {
        var start = SystemClock.elapsedRealtime()
        rvCategoriesAdapter.submitList(categories)
        println("tiempo categories: ${SystemClock.elapsedRealtime() - start} ms")
    }

    private fun updateCategorySelected(idCategorySelected: Long) {
        viewModel.onEvent(TaskUiEvent.UpdateCategoryTaskSelected(idCategorySelected))
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
        viewModel.onEvent(TaskUiEvent.UpdateCompletedTask(id, isCompleted))
    }

    private fun setRvAdapter() {
        rvAdapter = TaskListAdapter(callbacks)
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvTasks)

    }

    private fun setRvCategoriesAdapter() {
        rvCategoriesAdapter =
            CategoriesListAdapter(::openAddCategoryDialog, ::updateCategorySelected)
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = rvCategoriesAdapter
        }

    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0, /*ItemTouchHelper.LEFT or*/ ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            println("1111")
            return false // No necesitamos drag & drop
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            println("2222")
            val id = viewHolder.itemId
            viewModel.onEvent(TaskUiEvent.DeletedTask(id))
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
            println("3333")
            // Aplica la animación nativa de Material Design al deslizar
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

}