package com.example.todotasks.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["task"], unique = true)])
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Int,
    @ColumnInfo("task") val task: String
)