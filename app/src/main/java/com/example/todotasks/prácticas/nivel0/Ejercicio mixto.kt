package com.example.todotasks.prácticas.nivel0

/*
Programa que:
1. Pida un número
2. Si es par → imprime tabla de multiplicar con for
3. Si es impar → cuenta regresiva con while
 */

fun main() {
    print("Introduce un número: ")
    val num = readLine()?.toInt() ?: 0

    if (num % 2 == 0) {
        imprimirTabla(num)
    } else {
        cuentaRegresiva(num)
    }

}

fun imprimirTabla(num: Int) {
    for (i in 1..10) {
        println("$i * $num = ${i * num}")
    }
}

fun cuentaRegresiva(num: Int) {
    var numRegresivo = num
    while (numRegresivo >= 0) {
        println(numRegresivo)
        numRegresivo--
    }
}