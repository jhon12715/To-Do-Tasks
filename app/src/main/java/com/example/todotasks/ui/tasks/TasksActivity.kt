package com.example.todotasks.ui.tasks

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todotasks.databinding.ActivityTasksBinding
import com.example.todotasks.ui.tasks.Dialog.DialogTasks
import com.example.todotasks.ui.tasks.model.TaskItem

class TasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTasksBinding
    private var lista: MutableList<TaskItem> = mutableListOf()
    private lateinit var rvAdapter: TasksAdapter
    private val tasksViewModel: TasksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvAdapter = TasksAdapter(lista)
        lista.add(
            TaskItem(
                "panpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpanpan",
                true
            )
        )
        lista.add(TaskItem("café", false))

        setListeners()
        setRvAdapter()
    }

    private fun setListeners() {

        binding.fabTask.setOnClickListener {
            DialogTasks().show(supportFragmentManager, "")
        }


    }

    private fun addTask(task: String) {
        //lista.add(task)
        updateTasks()
    }

    private fun updateTasks() {
        rvAdapter.lista = lista
        rvAdapter.notifyDataSetChanged()
    }

    private fun setRvAdapter() {

        rvAdapter = TasksAdapter(lista)
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }

    }
}