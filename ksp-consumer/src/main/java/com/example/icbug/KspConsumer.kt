package com.example.icbug

import com.example.annotations.ApolloType

@ApolloType(DogsQuery.Dog::class)
object KspConsumer

fun main() {
    println(
        DogsQuery.Dog("d", 3).properties()
    )
}
