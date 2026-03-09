package com.example.todotasks.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "task", indices = [Index(value = ["task"], unique = true)])
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long = 0,
    @ColumnInfo("task", collate = ColumnInfo.NOCASE) val task: String,
    @ColumnInfo("isCompleted") val isCompleted: Boolean
)