package com.example.todotasks.ui.task

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.todotasks.R
import com.example.todotasks.databinding.ActivityTaskBinding
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.ui.core.extensions.backgroundMoreWhite
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.task.task_form.DialogTaskForm
import com.example.todotasks.ui.task.adapter.CategoriesListAdapter
import com.example.todotasks.ui.task.category_form.CategoryTaskSheet
import com.example.todotasks.ui.model.TypeCategoryUI
import com.example.todotasks.ui.task.adapter.PopupCategoryCallbacks
import com.example.todotasks.ui.task.list.TaskListViewModel
import com.example.todotasks.ui.task.list.adapter.TasksCollectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.example.todotasks.ui.task.list.TaskUiEvent

@AndroidEntryPoint
class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var rvCategoriesAdapter: CategoriesListAdapter
    private lateinit var tasksCollectionAdapter: TasksCollectionAdapter
    private val viewModel: TaskListViewModel by viewModels()
    private var toast: Toast? = null
    private lateinit var categoryForm: CategoryTaskSheet
    private lateinit var taskForm: DialogTaskForm

    private val popupCategoryCallbacks by lazy {
        PopupCategoryCallbacks(
            onEdit = ::editCategory,
            onDelete = ::deleteCategory
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setRvAdapter()
        setRvCategoriesAdapter()
        setFlows()

        binding.clParent.backgroundMoreWhite(0.9f)
    }

    private fun setListeners() {

        binding.fabTask.setOnClickListener {
            openNewDialogTaskForm()
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
            offscreenPageLimit = 1

            // 3. Agregar padding lateral para mostrar la página anterior/siguiente
            setPadding(offset, 0, offset, 0)
        }
    }

    private fun setFlows() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoriesState.collect { categories ->
                    updateCategories(categories)
                    binding.vpTasks.post {
                        updateCollectionAdapter(categories)

                        if (binding.vpTasks.currentItem >= categories.size) {
                            binding.vpTasks.setCurrentItem(categories.lastIndex, false)
                        }
                        tasksCollectionAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskUIState.map { it.isLoading }
                    .distinctUntilChanged()
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

    private fun newCategory() {
        categoryForm = CategoryTaskSheet.newInstance(CategoryUI())
        openCategoryTaskSheet()
    }

    private fun editCategory(category: CategoryUI) {
        categoryForm = CategoryTaskSheet.newInstance(category)
        openCategoryTaskSheet()
    }
    private fun openCategoryTaskSheet() {
        categoryForm.show(supportFragmentManager, "")
    }

    private fun deleteCategory(category: CategoryUI) {
        viewModel.onEvent(TaskUiEvent.DeleteCategory(category))
    }

    private fun showToast(message: String) {
        binding.pbLoading.visibility = View.GONE
        toast?.cancel()
        toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        toast?.show()
    }

    private fun openNewDialogTaskForm() {
        taskForm = DialogTaskForm.newInstance(TaskUI())
        taskForm.show(supportFragmentManager, "")
    }

    private fun updateCategories(categories: List<TypeCategoryUI>) {
        rvCategoriesAdapter.submitList(categories)
    }

    private fun updateCollectionAdapter(category: List<TypeCategoryUI>) {
        val categoriesFiltered = category.filterIsInstance<TypeCategoryUI.CategoryItem>()
        tasksCollectionAdapter.submitList(categoriesFiltered)
    }

    private fun updateCategorySelected(idCategorySelected: Long, position: Int) {
        viewModel.onEvent(TaskUiEvent.UpdateCategoryTaskSelected(idCategorySelected))
        binding.vpTasks.currentItem = position
    }

    private fun setRvCategoriesAdapter() {
        rvCategoriesAdapter =
            CategoriesListAdapter(
                popupCategoryCallbacks,
                ::newCategory,
                ::updateCategorySelected
            )
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = rvCategoriesAdapter
        }

    }

}