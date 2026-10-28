package com.example.todotasks.domain.model.result_event

sealed class ResultEventCategory(override val isSuccess: Boolean): AppResultEvent {
    data object CreateSuccess : ResultEventCategory(true)
    data object UpdateSuccess : ResultEventCategory(true)
    data object DeleteSuccess : ResultEventCategory(true)
    data object CategoryAlreadyExistsError : ResultEventCategory(false)
}