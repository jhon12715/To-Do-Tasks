package com.example.todotasks.ui.model

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

enum class DateHeaderSorter (val num: Int, val header: String){
    HOY(1,"Hoy"),
    FUTURAS(2, "Futuras"),
    VENCIDAS(3, "Vencidas"),
    SIN_FECHA(4, "Sin Fecha")
}