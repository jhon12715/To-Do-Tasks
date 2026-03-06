package com.example.todotasks.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subTask",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["id"],
        childColumns = ["id_task"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(value = ["id_task", "title"], unique = true)]
)
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long = 0,
    @ColumnInfo("id_task") val idTask: Long,
    @ColumnInfo("title", collate = ColumnInfo.NOCASE) val title: String,
    @ColumnInfo("completed") val completed: Boolean = false
)