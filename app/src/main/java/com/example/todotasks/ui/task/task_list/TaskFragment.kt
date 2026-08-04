package com.example.todotasks.ui.task.task_list

import android.graphics.Canvas
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.databinding.ItemPageTaskBinding
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeTaskListItem
import com.example.todotasks.ui.subTask.list_subtask.SubTaskActivity
import com.example.todotasks.ui.task.TaskItemCallbacks
import com.example.todotasks.ui.task.dialog.DialogDeleteTask
import com.example.todotasks.ui.task.task_form.DialogTaskForm
import com.example.todotasks.ui.task.task_list.adapter.TaskListAdapter
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
            deleteTask = { task -> openDialogDeleteTask(task) },
            updateCompletedTask = { id, completed -> updateCompletedTask(id, completed) }
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

    override fun onStart() {
        super.onStart()
        Log.d("LIFECYCLE", "onStart $categoryId")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LIFECYCLE", "onStop $categoryId")
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
        println("entraaaa")
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvTasks)

    }

    private fun setFlows() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.getTasksByCategory(categoryId).collect { tasks ->
                    println("recibe: $tasks, size: ${tasks.size}")
                    if (tasks.isNotEmpty()) {
                        rvAdapter.submitList(tasks)
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

    private fun openDialogDeleteTask(task: Task) {
        DialogDeleteTask(task).show(parentFragmentManager, "")
    }

    private fun updateCompletedTask(id: Long, isCompleted: Boolean) {
        viewModel.onEvent(TaskUiEvent.UpdateCompletedTask(id, isCompleted))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            val id = viewHolder.itemId
            println("id: $id position: $position")
            viewModel.onEvent(TaskUiEvent.DeleteTask(id))
            // Llama a tu adaptador para eliminar el item
            // adapter.notifyItemRemoved(position)
        }

        override fun getSwipeDirs(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val position = viewHolder.adapterPosition

            // Evita errores si la posición ya no es válida en el adaptador
            if (position == RecyclerView.NO_POSITION) return 0

            // 1. Obtén el adaptador
            val adapter = recyclerView.adapter as? TaskListAdapter

            // 2. Obtén el ítem actual de tu lista de datos
            val item = adapter?.currentList?.getOrNull(position)

            // 3. Verifica si es instancia de la clase permitida
            if (item is TypeTaskListItem.TaskItem) {
                return super.getSwipeDirs(recyclerView, viewHolder) // Permite el swipe
            }

            return 0
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