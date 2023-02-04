package com.example.icbug

import org.junit.Test

class KspConsumerTest {

    @Test
    fun test() {
        // making ANY changes to this file and compiling the test will fail with:
        // Cannot access class 'com.example.icbug.DogsQuery.Dog'. Check your module classpath for missing or conflicting dependencies
        DogsQuery.Dog("dog", 3).properties()
    }
}