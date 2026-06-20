package com.example.todotasks.domain.usecase

import android.app.TaskInfo
import com.example.todotasks.domain.model.ParentTaskInfo
import com.example.todotasks.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class GetParentTaskInfoUseCase @Inject constructor(val repository: TaskRepository) {

    suspend operator fun invoke(id: Long): ParentTaskInfo = repository.getParentTaskInfo(id)
}