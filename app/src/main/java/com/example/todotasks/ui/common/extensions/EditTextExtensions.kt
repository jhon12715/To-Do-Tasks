package com.example.todotasks.ui.common.extensions

import android.text.InputFilter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

fun setSettingsEditTextOfForm(inputLayout: TextInputLayout, inputEditText: TextInputEditText, maxLenght: Int){

    val newFilters = InputFilter.LengthFilter(maxLenght)
    inputEditText.apply {
        maxLines = 4
        minLines = 1
        filters += newFilters
        isSingleLine = false
    }

    inputLayout.apply {
        isCounterEnabled = true
        counterMaxLength = maxLenght
    }

}