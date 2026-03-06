package com.example.todotasks.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todotasks.data.database.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Update
    suspend fun updateTask(task: TaskEntity)

}