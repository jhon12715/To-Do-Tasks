package com.example.todotasks.ui.task

import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
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
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.todotasks.R
import com.example.todotasks.databinding.ActivityTaskBinding
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.ui.task.viewPagerAdapter.TaskListAdapter
import com.example.todotasks.ui.task.dialog.DialogTask
import com.example.todotasks.ui.task.adapter.CategoriesListAdapter
import com.example.todotasks.ui.task.dialog.CategoryTaskSheet
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI
import com.example.todotasks.ui.task.viewPagerAdapter.TasksCollectionAdapter
import com.example.todotasks.ui.task.viewPagerAdapter.TasksFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var rvCategoriesAdapter: CategoriesListAdapter
    private lateinit var tasksCollectionAdapter: TasksCollectionAdapter
    private val viewModel: TaskViewModel by viewModels()
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setRvAdapter()
        setRvCategoriesAdapter()
        setFlows()
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

    private fun setRvAdapter() {

        tasksCollectionAdapter = TasksCollectionAdapter(this)
        binding.vpTasks.adapter = tasksCollectionAdapter

        val pageMargin = resources.getDimensionPixelOffset(R.dimen.page_margin)
        val offset = resources.getDimensionPixelOffset(R.dimen.off_margin)

        binding.vpTasks.apply {
        // 1. Margen entre páginas
            setPageTransformer(MarginPageTransformer(pageMargin))

        // 2. Permitir que los bordes se dibujen fuera del padding
            clipToPadding = false
            clipChildren = false

        // 3. Agregar padding lateral para mostrar la página anterior/siguiente
            setPadding(offset, 0, offset, 0)
        }
    }

    private fun setFlows() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoriesState.collect { categories ->
                    updateCategories(categories)
                    updateCountCollectionAdapter(categories.size)
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

    private fun openNewTaskDialog() {
        viewModel.onEvent(
            TaskUiEvent.OpenNewTaskDialog
        )
        DialogTask().show(supportFragmentManager, "")
    }

    private fun updateCategories(categories: List<TypeCategoryUI>) {
        rvCategoriesAdapter.submitList(categories)
    }

    private fun updateCountCollectionAdapter(size: Int) {
        tasksCollectionAdapter.submitCount(size)
    }

    private fun updateCategorySelected(idCategorySelected: Long) {
        viewModel.onEvent(TaskUiEvent.UpdateCategoryTaskSelected(idCategorySelected))
    }

    private fun setRvCategoriesAdapter() {
        rvCategoriesAdapter =
            CategoriesListAdapter(::openAddCategoryDialog, ::updateCategorySelected)
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = rvCategoriesAdapter
        }

    }

}