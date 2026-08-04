package com.example.todotasks.data.database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.todotasks.domain.model.TaskPriority

@Entity(
    tableName = "subTask",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["id"],
        childColumns = ["idTask"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(value = ["idTask", "title"], unique = true)]
)
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long = 0,
    @ColumnInfo("idTask") val idTask: Long,
    @ColumnInfo("title", collate = ColumnInfo.NOCASE) val title: String,
    @ColumnInfo("priority") val priority: TaskPriority,
    @ColumnInfo("completed") val completed: Boolean = false
)