package com.example.todotasks.prácticas.nivel0

/*
1. Pide una edad
2. Si edad ≥ 18 → “Mayor de edad”
3. Si no → “Menor de edad”
👉 Extra:
    Si edad > 60 → “Jubilado”
*/

fun main(){
    print("Introduce tu edad: ")
    val edad: Int = readLine()?.toInt() ?: 1
    if (edad >= 18){
        print("Mayor de edad")
    }else{
        print("Menor de edad")
    }
}