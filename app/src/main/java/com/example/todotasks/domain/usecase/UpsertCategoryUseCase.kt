package com.example.todotasks.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.result_event.AppResultEvent
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.domain.model.result_event.ResultEventCategory
import com.example.todotasks.domain.model.result_event.ResultEventCommon
import javax.inject.Inject

class UpsertCategoryUseCase @Inject constructor(private val repository: TaskRepository) {

    suspend operator fun invoke(category: Category): AppResultEvent {

        if (isNotValidLenght(category.name)) return ResultEventCommon.LengthNotValidError

        val categoryTrim = category.name.trim()

        if (categoryTrim.isEmpty()) return ResultEventCommon.EmptyTextError

        return try {
            repository.upsertCategory(category)

            val isEditing = category.id > 0L
            return getSuccessMessage(isEditing)

        } catch (e: SQLiteConstraintException) {
            ResultEventCategory.CategoryAlreadyExistsError
        }
    }

    private fun isNotValidLenght(name: String) = name.length > Category.CATEGORY_TITTLE_MAX_LENGTH

    private fun getSuccessMessage(isEditing: Boolean): ResultEventCategory =
        if (isEditing)
            ResultEventCategory.UpdateSuccess
        else
            ResultEventCategory.CreateSuccess
}