package com.example.todotasks.ui.common.extensions

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */
fun getAppFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
