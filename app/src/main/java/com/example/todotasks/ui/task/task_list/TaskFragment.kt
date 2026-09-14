package com.example.todotasks.ui.task.task_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todotasks.databinding.ItemPageTaskBinding
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI
import com.example.todotasks.ui.subTask.subtask_list.SubTaskActivity
import com.example.todotasks.ui.task.TaskUiEvent
import com.example.todotasks.ui.task.task_list.adapter.TaskItemCallbacks
import com.example.todotasks.ui.task.task_form.DialogTaskForm
import com.example.todotasks.ui.task.task_list.adapter.TaskListAdapter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class TaskFragment() : Fragment() {

    companion object {
        const val COLLECTION_CATEGORY_ID = "COLLECTION_CATEGORY_ID"

        fun newInstance(categoryId: Long): TaskFragment {
            return TaskFragment().apply {
                arguments = Bundle().apply {
                    putLong(COLLECTION_CATEGORY_ID, categoryId)
                }
            }
        }
    }

    private var _binding: ItemPageTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskListViewModel by activityViewModels()
    private lateinit var rvAdapter: TaskListAdapter
    private var categoryId: Long = 0
    private lateinit var dialogTaskForm: DialogTaskForm

    private val callbacks: TaskItemCallbacks by lazy {
        TaskItemCallbacks(
            editTask = { taskUI ->
                openEditDialogTaskForm(taskUI)
            },
            openSubTaskActivity = { id, name -> openSubTaskById(id, name) },
            updateCompletedTask = { id, completed -> updateCompletedTask(id, completed) },
            updateScreenMode = { screenMode -> updateScreenMode(screenMode) },
            addTaskToDelete = { taskId -> addTaskToDelete(taskId) },
            removeTaskToDelete = { taskId -> removeTaskToDelete(taskId) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemPageTaskBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setFlows()

        arguments?.takeIf { it.containsKey(COLLECTION_CATEGORY_ID) }?.run {
            categoryId = getLong(COLLECTION_CATEGORY_ID)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.onEvent(TaskUiEvent.UpdateCategoryTaskSelected(categoryId))
    }

    private fun setAdapter() {
        rvAdapter = TaskListAdapter(callbacks)
        binding.rvTasks.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            this.adapter = rvAdapter
        }

    }

    private fun setFlows() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.taskUIState.map { it.selectedFilterCategoryId }
                        .distinctUntilChanged()
                        .collect { selectedFilterCategoryId ->
                            val position: Int =
                                viewModel.categoriesUI.value!!.indexOfFirst { category ->
                                    if (category is TypeCategoryUI.CategoryItem) {
                                        category.category.id == selectedFilterCategoryId
                                    } else {
                                        false
                                    }
                                }
                            binding.rvTasks.smoothScrollToPosition(position)
                        }
                }

                launch {
                    viewModel.getTasksByCategory(categoryId).collect { tasks ->
                        rvAdapter.submitList(tasks)

                    }
                }

                launch {
                    viewModel.taskUIState.map { it.screenMode }
                        .distinctUntilChanged()
                        .collect { screenMode ->

                            rvAdapter.updateScreenMode(screenMode)

                        }
                }
            }
        }
    }


    private fun openEditDialogTaskForm(taskUI: TaskUI) {
        dialogTaskForm = DialogTaskForm.newInstance(taskUI)
        dialogTaskForm.show(parentFragmentManager, "")
    }

    private fun openSubTaskById(id: Long, task: String) {

        val intent = SubTaskActivity.newIntent(requireContext(), id, task)

        this.startActivity(intent)
    }

    private fun updateCompletedTask(id: Long, isCompleted: Boolean) {
        viewModel.onEvent(TaskUiEvent.UpdateCompletedTask(id, isCompleted))
    }

    private fun updateScreenMode(screenMode: ScreenMode) {
        viewModel.onEvent(TaskUiEvent.UpdateScreenMode(screenMode))
    }

    private fun addTaskToDelete(taskId: Long) {
        viewModel.onEvent(TaskUiEvent.AddTaskToDelete(taskId))
    }

    private fun removeTaskToDelete(taskId: Long) {
        viewModel.onEvent(TaskUiEvent.RemoveTaskToDelete(taskId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}