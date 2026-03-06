package com.example.todotasks.prácticas.nivel0

    val letra = listOf<String>(
        "T",
        "R",
        "W",
        "A",
        "G",
        "M",
        "Y",
        "F",
        "P",
        "D",
        "X",
        "B",
        "N",
        "J",
        "Z",
        "S",
        "Q",
        "V",
        "H",
        "L",
        "C",
        "K",
        "E"
    )

    fun main(){
        print("Introduce tu DNI: ")
        val dni = readLine()!!.toInt()
        val posicion = dni % 23
        val letraDni = letra[posicion]
        println("La letra es $letraDni")
    }
