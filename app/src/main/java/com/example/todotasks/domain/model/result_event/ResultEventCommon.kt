package com.example.todotasks.domain.model.result_event

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class ResultEventCommon(override val isSuccess: Boolean) : AppResultEvent {
    data object LengthNotValidError : ResultEventCommon(false)
    data object EmptyTextError : ResultEventCommon(false)
}