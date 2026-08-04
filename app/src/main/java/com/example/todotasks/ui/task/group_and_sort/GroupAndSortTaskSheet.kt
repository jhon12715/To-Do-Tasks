package com.example.todotasks.ui.task.group_and_sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.R
import com.example.todotasks.databinding.GroupAndSorterTaskSheetBinding
import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.ui.task.task_list.TaskListViewModel
import com.example.todotasks.ui.task.task_list.TaskUiEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class GroupAndSortTaskSheet : BottomSheetDialogFragment() {

    private var _binding: GroupAndSorterTaskSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initFlows()
    }

    private fun initListeners() = with(binding) {
        btnGBPriority.setOnClickListener {
            viewModel.onEvent(TaskUiEvent.UpdateGroupBy(GroupByTask.PRIORITY))
        }
        btnGBDate.setOnClickListener {
            viewModel.onEvent(TaskUiEvent.UpdateGroupBy(GroupByTask.DATE))
        }
        btnOBTittle.setOnClickListener {
            viewModel.onEvent(TaskUiEvent.UpdateSortBy(SortByTask.TITTLE))
        }
        btnOBPriority.setOnClickListener {
            viewModel.onEvent(TaskUiEvent.UpdateSortBy(SortByTask.PRIORITY))
        }
        btnOBDate.setOnClickListener {
            viewModel.onEvent(TaskUiEvent.UpdateSortBy(SortByTask.DATE))
        }
    }

    private fun initFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.taskListPreferences.collect { taskListPreferences ->
                    val groupAndSort = taskListPreferences?.groupAndSort
                    putSelectedButton(groupAndSort!!.groupBy, groupAndSort.sortBy)
                }
            }
        }
    }

    private fun putSelectedButton(groupBy: GroupByTask, sortByTask: SortByTask) = with(binding) {
        val btnsGroupBy = listOf(btnGBPriority, btnGBDate)
        val btnsOrderBy = listOf(btnOBTittle, btnOBPriority, btnOBDate)

        val unselectedColor = ContextCompat.getColor(this.root.context, R.color.btnUnselected)
        val selectedColor = ContextCompat.getColor(this.root.context, R.color.btnSelected)

        val unSelectedTextColor = ContextCompat.getColor(this.root.context, R.color.black)
        val selectedTextColor = ContextCompat.getColor(this.root.context, R.color.white)

        btnsGroupBy.forEach {
            setBtnAttrib(it, unselectedColor, unSelectedTextColor)
        }

        btnsOrderBy.forEach {
            setBtnAttrib(it, unselectedColor, unSelectedTextColor)
        }

        when (groupBy) {
            GroupByTask.PRIORITY -> setBtnAttrib(btnGBPriority, selectedColor, selectedTextColor)
            GroupByTask.DATE -> setBtnAttrib(btnGBDate, selectedColor, selectedTextColor)
        }

        when (sortByTask) {
            SortByTask.TITTLE -> setBtnAttrib(btnOBTittle, selectedColor, selectedTextColor)
            SortByTask.PRIORITY -> setBtnAttrib(btnOBPriority, selectedColor, selectedTextColor)
            SortByTask.DATE -> setBtnAttrib(btnOBDate, selectedColor, selectedTextColor)
        }

    }

    private fun setBtnAttrib(btn: Button, backgroundColor: Int, textColor: Int) {
        btn.apply {
            setBackgroundColor(backgroundColor)
            setTextColor(textColor)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GroupAndSorterTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}