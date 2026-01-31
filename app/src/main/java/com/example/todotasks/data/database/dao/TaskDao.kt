package com.example.todotasks.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todotasks.data.database.entities.TaskEntity

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getTasks(): List<TaskEntity>

    @Insert
    fun insertTask(task: TaskEntity)

    @Query("DELETE FROM task WHERE id = :id")
    fun deleteTask(id: Long)

    @Update
    fun updateTask(task: TaskEntity)

}