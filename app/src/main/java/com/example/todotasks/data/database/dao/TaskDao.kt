package com.example.todotasks.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todotasks.data.database.entities.TaskEntity
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.task.model.TaskUI
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {

    @Query("SELECT t.id, t.task, (SELECT COUNT(*) FROM subtask s WHERE s.idTask = t.id AND s.completed = 1) as completedSubtask, (SELECT COUNT(*) FROM subtask s WHERE s.idTask = t.id) as totalSubTask, isCompleted, priority, date FROM task t")
    fun getAllTasks(): Flow<List<TaskUI>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTask(id: Long): Task

    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Query("UPDATE task SET task = :task, priority = :priority, date = :date WHERE id = :id")
    suspend fun updateTask(id: Long, task: String, priority: TaskPriority, date: LocalDate?)

    @Query("UPDATE task SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletedTask(id: Long, isCompleted: Boolean)

}