package com.example.todotasks.domain.model.result_event

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed interface AppResultEvent {
    val isSuccess: Boolean
}