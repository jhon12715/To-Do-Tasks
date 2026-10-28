package com.example.todotasks.ui.common

import com.example.todotasks.domain.model.SortOrder

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

fun <T, R : Comparable<R>, S : Comparable<S>> List<T>.sortByOrder(
    sortOrder: SortOrder,
    first: (T) -> R,
    second: (T) -> S
): List<T> =
    this.sortedWith(
        if (sortOrder == SortOrder.ASC) {
            compareBy(first)
        } else {
            compareByDescending(first)
        }
            .thenBy(second)
    )