package com.example.todotasks.ui.task.more_vert

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.todotasks.databinding.PopupMoreVertBinding
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.task.adapter.PopupCategoryCallbacks

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class PopupMenuMoreVert (context: Context) {

    private val binding = PopupMoreVertBinding.inflate(LayoutInflater.from(context))

    private val popup: PopupWindow = PopupWindow(
        binding.root,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    ).apply {
        isOutsideTouchable = true
    }

    fun show(
        anchor: View,
        popupMoreVertCallbacks: PopupMoreVertCallbacks
    ) = with(binding) {
        ivGroupAndFilter.setOnClickListener {
            popupMoreVertCallbacks.groupAndOrder()
            popup.dismiss()
        }

        tvGroupAndFilter.setOnClickListener {
            popupMoreVertCallbacks.groupAndOrder()
            popup.dismiss()
        }

        root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        // 2. Calcular la diferencia de anchos
        val anchoPopup = root.measuredWidth
        val desplazarX = -anchoPopup + (anchor.width / 2)
        val desplazarY = -(anchor.height / 2)

        popup.showAsDropDown(anchor, desplazarX, desplazarY)
    }

}