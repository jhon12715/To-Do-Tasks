package com.example.todotasks.domain.model

data class Category(
    val id: Long = 0L,
    val name: String
){
    companion object{
        const val CATEGORY_TITTLE_MAX_LENGTH = 60
    }
}