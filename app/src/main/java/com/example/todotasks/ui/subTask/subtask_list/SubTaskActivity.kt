package com.example.todotasks.ui.subTask.subtask_list

import android.content.Context
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
import com.example.todotasks.R
import com.example.todotasks.databinding.ActivitySubtaskBinding
import com.example.todotasks.domain.model.IsCompletedFilter
import com.example.todotasks.ui.subTask.DialogDeleteSubtaskCommit
import com.example.todotasks.ui.core.extensions.backgroundMoreWhite
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.subTask.subtask_list.adapter.SubTaskListAdapter
import com.example.todotasks.ui.subTask.subtask_form.DialogSubTaskForm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubTaskActivity : AppCompatActivity() {

    private val viewModel: SubTaskViewModel by viewModels()

    companion object {
        const val EXTRA_TASK_ID = "EXTRA_TASK_ID"
        const val EXTRA_TASK_NAME = "EXTRA_TASK_NAME"

        fun newIntent(context: Context, taskId: Long, taskName: String) =
            Intent(context, SubTaskActivity::class.java).apply {
                putExtra(EXTRA_TASK_ID, taskId)
                putExtra(EXTRA_TASK_NAME, taskName)
            }

    }

    private lateinit var binding: ActivitySubtaskBinding
    private lateinit var rvAdapter: SubTaskListAdapter
    private var toast: Toast? = null

    private val callbacks: SubtaskItemCallbacks by lazy {
        SubtaskItemCallbacks(
            updateSubTaskCompleted = { id, isCompleted -> updateSubTaskCompleted(id, isCompleted) },
            editSubTaskForm = { subTaskId -> editSubtaskForm(subTaskId) },
            addSubTaskToDeleteList = { subTaskId -> addSubTaskToDeleteList(subTaskId) },
            removeSubTaskToDeleteList = { subTaskId -> removeSubTaskToDeleteList(subTaskId) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubtaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taskName: String = intent.getStringExtra(EXTRA_TASK_NAME) ?: ""
        val taskId: Long = intent.getLongExtra(EXTRA_TASK_ID, -1L)

        viewModel.setParentTaskInfo(taskId, taskName)
        initUI(taskName)
        setListeners()
        setFlows()
        setAdapter()
    }

    private fun initUI(taskName: String) = with(binding) {
        tvTaskName.text = taskName
        clParent.backgroundMoreWhite(0.9f)
    }

    private fun setListeners() = with(binding) {
        fabTask.setOnClickListener {
            newSubtaskForm()
        }

        rgTaskFilter.setOnCheckedChangeListener { _, checkedId ->
            val filter = when (checkedId) {
                R.id.rbAll -> IsCompletedFilter.ALL
                R.id.rbCompletedTasks -> IsCompletedFilter.COMPLETED
                R.id.rbNotCompletedTasks -> IsCompletedFilter.NOT_COMPLETED
                else -> IsCompletedFilter.ALL
            }
            viewModel.onEvent(SubTaskUiEvent.UpdateFilterSubTaskCompleted(filter))
        }

        ivDeleteSubTask.setOnClickListener {
            DialogDeleteSubtaskCommit().show(supportFragmentManager, "")
        }
    }

    private fun editSubtaskForm(subTaskId: Long) {
        DialogSubTaskForm.newInstance(subTaskId = subTaskId).show(supportFragmentManager, "")
    }

    private fun newSubtaskForm() {
        val taskId = viewModel.getTaskId()
        DialogSubTaskForm.newInstance(taskId = taskId).show(supportFragmentManager, "")
    }

    private fun addSubTaskToDeleteList(subTaskId: Long) {
        viewModel.onEvent(SubTaskUiEvent.AddSubTaskToDeleteList(subTaskId))
    }

    private fun removeSubTaskToDeleteList(subTaskId: Long) {
        viewModel.onEvent(SubTaskUiEvent.RemoveSubTaskToDeleteList(subTaskId))
    }

    private fun setFlows() {

        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.subTaskUiState.map { it.listSubtasks }
                        .filterNotNull()
                        .distinctUntilChanged()
                        .collect { subTaskList ->
                            rvAdapter.submitList(subTaskList)
                        }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.subTaskUiState.map { it.subTaskCompletedText }
                        .distinctUntilChanged()
                        .collect { subTaskCompleted ->
                            binding.tvSubTasksCompleted.text = subTaskCompleted
                        }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.subTaskUiState.map { it.subTaskToDeleteText }
                        .distinctUntilChanged()
                        .collect { subTaskToDelete ->
                            binding.tvDeleteCount.text = subTaskToDelete
                        }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.subTaskEventResult
                        .collect { eventResult ->
                            showToast(eventResult.message)
                        }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.subTaskUiState.map { it.screenMode }
                        .distinctUntilChanged()
                        .collect { screenMode ->
                            setViewVisibilityByScreenMode(
                                binding.clLoading,
                                screenMode,
                                ScreenMode.LOADING
                            )

                            setViewVisibilityByScreenMode(
                                binding.tvCompleted,
                                screenMode,
                                ScreenMode.NORMAL
                            )
                            setViewVisibilityByScreenMode(
                                binding.tvSubTasksCompleted,
                                screenMode,
                                ScreenMode.NORMAL
                            )

                            setViewVisibilityByScreenMode(
                                binding.tvDelete,
                                screenMode,
                                ScreenMode.DELETE
                            )
                            setViewVisibilityByScreenMode(
                                binding.tvDeleteCount,
                                screenMode,
                                ScreenMode.DELETE
                            )

                            setViewVisibilityByScreenMode(
                                binding.ivDeleteSubTask,
                                screenMode,
                                ScreenMode.DELETE
                            )

                            rvAdapter.setScreenMode(screenMode)

                        }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.subTaskUiState.map { it.subTaskSelectedToDelete }
                        .distinctUntilChanged()
                        .collect { subTaskSelectedToDelete ->
                            val isListEmpty = subTaskSelectedToDelete == 0

                            binding.ivDeleteSubTask.apply {
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
            }
        }

    }

    private fun setViewVisibilityByScreenMode(
        view: View,
        screenMode: ScreenMode,
        viewExistsScreenMode: ScreenMode
    ) {
        view.visibility = if (screenMode == viewExistsScreenMode) View.VISIBLE else View.INVISIBLE
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun updateSubTaskCompleted(id: Long, isChecked: Boolean) {
        viewModel.onEvent(SubTaskUiEvent.UpdateSubTaskCompleted(id, isChecked))
    }

    private fun setAdapter() {
        rvAdapter = SubTaskListAdapter(callbacks)
        binding.rvSubTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }
    }

    override fun onBackPressed() {
        if (viewModel.subTaskUiState.value.screenMode == ScreenMode.DELETE) {
            viewModel.onEvent(SubTaskUiEvent.ExitFromDeleteMode)
        } else {
            super.onBackPressed()
        }
    }

}