package com.example.todotasks.domain.model.result_event

sealed class ResultEventTask(override val isSuccess: Boolean) : AppResultEvent {
    data object TaskAlreadyExistsError : ResultEventTask(false)
    data object IsEmptyError : ResultEventTask(false)
    data object DeleteOneTaskSuccess : ResultEventTask(true)
    data object DeleteMultipleTaskSuccess : ResultEventTask(true)
    data object CreateSuccess : ResultEventTask(true)
    data object UpdateSuccess : ResultEventTask(true)
}