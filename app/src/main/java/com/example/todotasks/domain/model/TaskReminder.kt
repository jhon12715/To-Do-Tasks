package com.example.todotasks.domain.model

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

enum class TaskReminder(val daysBefore: Long){
SEVEN_DAYS(7L),
    TWO_DAYS(2L),
    ONE_DAY(1L)
}