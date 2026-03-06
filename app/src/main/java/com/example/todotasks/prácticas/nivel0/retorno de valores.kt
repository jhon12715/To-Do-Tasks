package com.example.todotasks.prácticas.nivel0

/*Función que:
1. Reciba 2 números
2. Retorne el mayor
👉 Extra:
Función que retorne si un número es par
 */

fun main() {
    val numMayor = mayor(2, 4)
    println("el número mayor es $numMayor")
    val esPar = esPar(numMayor)
}

fun mayor(num1: Int, num2: Int): Int {
    val numMayor = if (num1 > num2) {
        num1
    } else {
        num2
    }
    return numMayor
}

fun esPar(num: Int): Boolean {
    return if (num % 2 == 0) {
        println("Es par")
        true
    } else {
        println("No es par")
        false
    }
}