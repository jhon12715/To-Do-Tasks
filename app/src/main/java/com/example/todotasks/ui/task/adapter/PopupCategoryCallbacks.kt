package com.example.todotasks.ui.task.adapter

import com.example.todotasks.domain.model.Category
import com.example.todotasks.ui.model.CategoryUI

data class PopupCategoryCallbacks (val onEdit:(CategoryUI) -> Unit, val onDelete:(CategoryUI) -> Unit)
