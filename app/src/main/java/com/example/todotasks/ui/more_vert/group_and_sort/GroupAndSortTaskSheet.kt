package com.example.todotasks.ui.more_vert.group_and_sort

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.R
import com.example.todotasks.databinding.GroupAndSorterTaskSheetBinding
import com.example.todotasks.domain.model.GroupBySubTask
import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortBySubTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.domain.model.SortOrder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@AndroidEntryPoint
class GroupAndSortTaskSheet : BottomSheetDialogFragment() {

    private var _binding: GroupAndSorterTaskSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroupAndSortViewModel by viewModels()
    private lateinit var groupAndSortType: GroupAndSortType

    companion object {
        const val GROUP_AND_SORT_TYPE = "GROUP_AND_SORT_TYPE"
        fun newInstance(groupAndSortType: GroupAndSortType): GroupAndSortTaskSheet =
            GroupAndSortTaskSheet().apply {
                arguments = Bundle().apply {
                    putSerializable(GROUP_AND_SORT_TYPE, groupAndSortType)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getItemType()
        initUI()
        initListeners()
        initFlows()
    }

    private fun initUI() {
        showButtons()
    }

    private fun showButtons() = with(binding) {
        if (groupAndSortType == GroupAndSortType.SUBTASK) {
            btnGBDate.visibility = View.GONE
            btnOBDate.visibility = View.GONE
        }
    }

    private fun getItemType() {
        groupAndSortType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(GROUP_AND_SORT_TYPE, GroupAndSortType::class.java)
        } else {
            arguments?.getSerializable(GROUP_AND_SORT_TYPE) as? GroupAndSortType
        } ?: GroupAndSortType.TASK
    }

    private fun initListeners() = with(binding) {
        btnGBPriority.setOnClickListener {
            if (groupAndSortType == GroupAndSortType.TASK) {
                viewModel.onEvent(GroupAndSortEvent.UpdateGroupByTask(GroupByTask.PRIORITY))
            } else {
                viewModel.onEvent(GroupAndSortEvent.UpdateGroupBySubTask(GroupBySubTask.PRIORITY))
            }
        }
        btnGBDate.setOnClickListener {
            viewModel.onEvent(GroupAndSortEvent.UpdateGroupByTask(GroupByTask.DATE))
        }
        btnOBTittle.setOnClickListener {
            if (groupAndSortType == GroupAndSortType.TASK) {
                viewModel.onEvent(GroupAndSortEvent.UpdateSortByTask(SortByTask.TITTLE))
            } else {
                viewModel.onEvent(GroupAndSortEvent.UpdateSortBySubTask(SortBySubTask.TITTLE))
            }
        }
        btnOBPriority.setOnClickListener {
            if (groupAndSortType == GroupAndSortType.TASK) {
                viewModel.onEvent(GroupAndSortEvent.UpdateSortByTask(SortByTask.PRIORITY))
            } else {
                viewModel.onEvent(GroupAndSortEvent.UpdateSortBySubTask(SortBySubTask.PRIORITY))
            }
        }
        btnOBDate.setOnClickListener {
            viewModel.onEvent(GroupAndSortEvent.UpdateSortByTask(SortByTask.DATE))
        }

        tvSortOrder.setOnClickListener {
            if (groupAndSortType == GroupAndSortType.TASK) {
                viewModel.onEvent(GroupAndSortEvent.UpdateSortOrderTask)
            } else {
                viewModel.onEvent(GroupAndSortEvent.UpdateSortOrderSubTask)
            }
        }
    }

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (groupAndSortType == GroupAndSortType.TASK) {
                    viewModel.groupAndSortTask
                        .filterNotNull()
                        .collect { groupAndSort ->
                            putSelectedButton(groupAndSort.groupBy, groupAndSort.sortBy)
                            setSortOrderText(groupAndSort.sortOrder, groupAndSort.sortBy)
                        }
                } else {
                    viewModel.groupAndSortSubTask
                        .filterNotNull()
                        .collect { groupAndSort ->
                            putSelectedButton(groupAndSort.groupBy, groupAndSort.sortBy)
                            setSortOrderText(groupAndSort.sortOrder, groupAndSort.sortBy)
                        }
                }
            }
        }
    }

    private fun putSelectedButton(groupBy: Any, sortBy: Any) =
        with(binding) {
            val btnsGroupBy = listOf(btnGBPriority, btnGBDate)
            val btnsOrderBy = listOf(btnOBTittle, btnOBPriority, btnOBDate)

            val unselectedColor =
                ContextCompat.getColor(this.root.context, R.color.btnUnselected)
            val selectedColor =
                ContextCompat.getColor(this.root.context, R.color.btnSelected)

            val unSelectedTextColor =
                ContextCompat.getColor(this.root.context, R.color.black)
            val selectedTextColor =
                ContextCompat.getColor(this.root.context, R.color.white)

            btnsGroupBy.forEach {
                setBtnAttrib(it, unselectedColor, unSelectedTextColor)
            }

            btnsOrderBy.forEach {
                setBtnAttrib(it, unselectedColor, unSelectedTextColor)
            }

            when (groupBy) {
                GroupByTask.PRIORITY,
                GroupBySubTask.PRIORITY -> setBtnAttrib(
                    btnGBPriority,
                    selectedColor,
                    selectedTextColor
                )

                GroupByTask.DATE -> setBtnAttrib(
                    btnGBDate,
                    selectedColor,
                    selectedTextColor
                )
            }

            when (sortBy) {
                SortByTask.TITTLE,
                SortBySubTask.TITTLE -> setBtnAttrib(
                    btnOBTittle,
                    selectedColor,
                    selectedTextColor
                )

                SortByTask.PRIORITY,
                SortBySubTask.PRIORITY -> setBtnAttrib(
                    btnOBPriority,
                    selectedColor,
                    selectedTextColor
                )

                SortByTask.DATE -> setBtnAttrib(
                    btnOBDate,
                    selectedColor,
                    selectedTextColor
                )
            }

        }

    private fun setBtnAttrib(btn: Button, backgroundColor: Int, textColor: Int) {
        btn.apply {
            setBackgroundColor(backgroundColor)
            setTextColor(textColor)
        }
    }

    private fun setSortOrderText(sortOrder: SortOrder, sortBy: Any) =
        with(binding) {
            tvSortOrder.text = when (sortBy) {
                SortByTask.TITTLE,
                SortBySubTask.TITTLE -> if (sortOrder == SortOrder.ASC) "Descendente" else "Ascendente"

                SortByTask.PRIORITY,
                SortBySubTask.PRIORITY -> if (sortOrder == SortOrder.ASC) "Menor a mayor" else "Mayor a menor"

                SortByTask.DATE -> if (sortOrder == SortOrder.ASC) "Antigua a reciente" else "Reciente a antigua"

                else -> ""
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