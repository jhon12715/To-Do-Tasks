package com.example.todotasks.data.database.room.converters

import androidx.room.TypeConverter
import com.example.todotasks.domain.model.TaskPriority
import java.time.LocalDate

class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: TaskPriority): Int{
        return priority.num
    }

    @TypeConverter
    fun toPriority(num: Int): TaskPriority {
        return TaskPriority.entries.first { it.num == num }
    }

    // EpochDay es más eficiente
    @TypeConverter
    fun fromLocalDate(dueDate: LocalDate?): Long?{
        return dueDate?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? {
        return epochDay?.let { LocalDate.ofEpochDay(it) }
    }
}