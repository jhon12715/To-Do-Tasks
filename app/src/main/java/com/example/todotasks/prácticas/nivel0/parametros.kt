package com.example.todotasks.prácticas.nivel0

/*
Función que:
1. Reciba nombre y edad
2. Imprima: “Hola X tienes Y años”
 */

fun main() {
    datos("Jhon", 23)
}

fun datos(nombre: String, edad: Int) {
    println("Hola $nombre tienes $edad años")
}