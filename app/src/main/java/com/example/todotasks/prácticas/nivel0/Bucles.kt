package com.example.todotasks.prácticas.nivel0

/*
1. Cuenta del 1 al 10 con while
2. Imprime números pares del 1 al 20 con for
👉 Extra:
Suma números del 1 al 100
 */

fun main(){
    var num: Int = 1
    while(num <= 10){
        println(num)
        num++
    }
    for(i in 2..20 step 2){
        println(i)
    }

    var num2: Int = 1
    var num3 = 0
    while (num2 <= 100){
        num3 += num2
        println(num3)
        num2 ++

    }
}