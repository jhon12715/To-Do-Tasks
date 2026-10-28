package com.example.todotasks.domain.model.result_event

sealed class ResultEventSubtask(override val isSuccess: Boolean): AppResultEvent {
    data object SubtaskAlreadyExistsError : ResultEventSubtask(false)
    data object IsEmptyError : ResultEventSubtask(false)
    data object DeleteOneSubtaskSuccess : ResultEventSubtask(true)
    data object DeleteMultipleSubtaskSuccess : ResultEventSubtask(true)
    data object CreateSuccess : ResultEventSubtask(true)
    data object UpdateSuccess : ResultEventSubtask(true)
}