package com.example.todotasks.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todotasks.data.database.entities.TaskEntity
import com.example.todotasks.ui.task.model.TaskUI
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT t.id, t.task, (SELECT COUNT(*) FROM subtask s WHERE s.idTask = t.id AND s.completed = 1) as completedSubtask, (SELECT COUNT(*) FROM subtask s WHERE s.idTask = t.id) as totalSubTask, isCompleted FROM task t ORDER BY id ASC")
    fun getTasks(): Flow<List<TaskUI>>

    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE task SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletedTask(id: Long, isCompleted: Boolean)

}