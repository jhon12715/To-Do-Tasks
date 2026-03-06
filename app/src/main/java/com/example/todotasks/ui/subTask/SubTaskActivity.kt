package com.example.todotasks.ui.subTask

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todotasks.databinding.ActivitySubtaskBinding
import com.example.todotasks.databinding.ActivityTaskBinding
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.ui.subTask.adapter.SubTaskAdapter
import com.example.todotasks.ui.subTask.dialog.DialogDeleteSubtask
import com.example.todotasks.ui.subTask.dialog.DialogSubTask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubTaskActivity : AppCompatActivity() {

    private val viewModel: SubTaskViewModel by viewModels()

    companion object {
        const val EXTRA_TASK_ID = "EXTRA_TASK_ID"
        const val EXTRA_TASK_NAME = "EXTRA_TASK_NAME"
    }

    private lateinit var binding: ActivitySubtaskBinding
    private lateinit var rvAdapter: SubTaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubtaskBinding.inflate(layoutInflater)
        val taskName: String = intent.getStringExtra(EXTRA_TASK_NAME) ?: ""
        val idTask: Long = intent.getLongExtra(EXTRA_TASK_ID, -1L)
        setContentView(binding.root)
        startUI(taskName)
        setListeners(idTask)
        setFlows()
        setAdapter(idTask)
        tvSubTasksCompleted()
    }

    private fun startUI(taskName: String) {
        binding.tvTaskName.text = taskName
    }

    private fun setListeners(idTask: Long) {
        binding.fabTask.setOnClickListener {
            newSubTaskDialog(idTask)
        }
    }

    private fun newSubTaskDialog(idTask: Long) {
        DialogSubTask(idTask = idTask).show(supportFragmentManager, "")
    }

    private fun updateSubtaskDialog(idSubTask: Long, text: String) {
        DialogSubTask(idSubTask = idSubTask, subTaskName = text).show(supportFragmentManager, "")
    }

    private fun deleteSubtaskDialog(idTask: Long, text: String) {
        DialogDeleteSubtask(idTask, text).show(supportFragmentManager, "")
    }

    private fun setFlows() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listaSubTasks.collect { subTasks ->
                    updateRvAdapter(subTasks)
                    tvSubTasksCompleted()
                }
            }
        }

    }

    private fun updateSubTaskCompleted(id: Long, isChecked: Boolean) {
        viewModel.updateSubTaskCompleted(id, isChecked)
    }

    private fun tvSubTasksCompleted() {
        binding.tvSubTasksCompleted.text = viewModel.getCompletedSubTasks()
    }

    private fun updateRvAdapter(subTasks: List<SubTask>) {
        rvAdapter.lista = subTasks
        rvAdapter.notifyDataSetChanged()
    }

    private fun setAdapter(idTask: Long) {
        rvAdapter = SubTaskAdapter(
            updateSubTaskCompleted = ::updateSubTaskCompleted,
            updateSubTaskName = ::updateSubtaskDialog,
            deleteSubtask = ::deleteSubtaskDialog
        )
        binding.rvSubTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }
        val subTasks = viewModel.getSubTasks(idTask)
    }

}