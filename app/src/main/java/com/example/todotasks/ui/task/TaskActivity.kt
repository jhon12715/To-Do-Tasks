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
import com.example.todotasks.domain.model.IsCompletedFilter
import com.example.todotasks.ui.core.extensions.backgroundMoreWhite
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.task.task_form.DialogTaskForm
import com.example.todotasks.ui.task.adapter.CategoriesListAdapter
import com.example.todotasks.ui.task.category_form.CategoryTaskSheet
import com.example.todotasks.ui.model.TypeCategoryUI
import com.example.todotasks.ui.task.adapter.PopupCategoryCallbacks
import com.example.todotasks.ui.task.dialog.DialogDeleteTaskCommit
import com.example.todotasks.ui.task.group_and_sort.GroupAndSortTaskSheet
import com.example.todotasks.ui.task.task_list.TaskListViewModel
import com.example.todotasks.ui.task.task_list.adapter.TasksCollectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.example.todotasks.ui.task.more_vert.PopupMenuMoreVert
import com.example.todotasks.ui.task.more_vert.PopupMoreVertCallbacks
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var rvCategoriesAdapter: CategoriesListAdapter
    private lateinit var tasksCollectionAdapter: TasksCollectionAdapter
    private val viewModel: TaskListViewModel by viewModels()
    private var toast: Toast? = null
    private lateinit var categoryForm: CategoryTaskSheet
    private val groupAndFilter: GroupAndSortTaskSheet by lazy { GroupAndSortTaskSheet() }
    private lateinit var taskForm: DialogTaskForm

    private val popupCategoryCallbacks by lazy {
        PopupCategoryCallbacks(
            onEdit = ::editCategory,
            onDelete = ::deleteCategory
        )
    }

    private val popupMoreVertCallbacks by lazy {
        PopupMoreVertCallbacks(
            groupAndOrder = ::openGroupAndFilterTaskSheet
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        setListeners()
        setRvAdapter()
        setRvCategoriesAdapter()
        setFlows()


    }

    private fun initUI() {
        binding.clParent.backgroundMoreWhite(0.9f)
    }

    private fun setListeners() {

        binding.fabTask.setOnClickListener {
            openNewDialogTaskForm()
        }

        binding.ivMoreVert.setOnClickListener {
            PopupMenuMoreVert(binding.root.context).show(binding.ivMoreVert, popupMoreVertCallbacks)
        }

        binding.rgTaskFilter.setOnCheckedChangeListener { _, checkedId ->
            val filter = when (checkedId) {
                R.id.rbAll -> IsCompletedFilter.ALL
                R.id.rbCompletedTasks -> IsCompletedFilter.COMPLETED
                R.id.rbNotCompletedTasks -> IsCompletedFilter.NOT_COMPLETED
                else -> IsCompletedFilter.ALL
            }
            viewModel.onEvent(TaskUiEvent.UpdateTaskIsCompletedFilter(filter))
        }

        binding.ivDeleteTask.setOnClickListener {
            DialogDeleteTaskCommit().show(
                supportFragmentManager,
                ""
            )
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
                launch {
                    viewModel.categoriesUI
                        .filterNotNull()
                        .collect { categories ->
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

                launch {
                    viewModel.taskUIState.map { it.screenMode }
                        .distinctUntilChanged()
                        .collect { screenMode ->

                            binding.viewIsLoading.isVisible = screenMode == ScreenMode.LOADING
                            val isVisibleIfIsDeleteMode =
                                if (screenMode != ScreenMode.DELETE) View.GONE else View.VISIBLE
                            binding.tvSelectedToDelete.visibility = isVisibleIfIsDeleteMode
                            binding.ivDeleteTask.visibility = isVisibleIfIsDeleteMode

                        }
                }

                launch {
                    viewModel.taskUIState.map { it.tasksSelectedToDeleteText }
                        .distinctUntilChanged()
                        .collect { tasksSelectedToDeleteText ->
                            binding.tvSelectedToDelete.text = tasksSelectedToDeleteText
                        }
                }

                launch {
                    viewModel.taskUIState.map { it.tasksSelectedToDelete }
                        .distinctUntilChanged()
                        .collect { tasksSelectedToDelete ->
                            val isListEmpty = tasksSelectedToDelete.isEmpty()
                            binding.ivDeleteTask.apply {
                                isClickable = !isListEmpty
                                setAlpha(
                                    if (isListEmpty) {
                                        128
                                    } else {
                                        255
                                    }
                                )
                            }
                        }
                }

                launch {
                    viewModel.resultEvent.collect { result ->

                        showToast(result.message)
                    }
                }

            }

        }
    }

    //PopupCategory
    private fun newCategory() {
        categoryForm = CategoryTaskSheet.newInstance(CategoryUI())
        openCategoryTaskSheet()
    }

    private fun editCategory(category: CategoryUI) {
        categoryForm = CategoryTaskSheet.newInstance(category)
        openCategoryTaskSheet()
    }

    //PopupMoreVert
    private fun openGroupAndFilterTaskSheet() {
        groupAndFilter.show(supportFragmentManager, "")
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
        val currentCategoryId = viewModel.taskUIState.value.selectedFilterCategoryId
        taskForm = DialogTaskForm.newInstance(TaskUI(categoryId = currentCategoryId))
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

    override fun onBackPressed() {
        val currentScreenMode = viewModel.taskUIState.value.screenMode
        if (currentScreenMode == ScreenMode.DELETE) viewModel.onEvent(
            TaskUiEvent.UpdateScreenMode(
                ScreenMode.NORMAL
            )
        ) else super.onBackPressed()
    }

}